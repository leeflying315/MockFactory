package com.lifei.verticle;

import com.alibaba.fastjson.JSON;
import com.lifei.bean.Config;
import com.lifei.bean.MetricRateBean;
import com.lifei.bean.SocketSessionBean;
import com.lifei.constant.MqttTopicConstant;
import com.lifei.utils.Method;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/2/19
 */
@Log4j2
public class TcpBenchMarkVerticle extends AbstractVerticle {
    private Stack<NetClient> socketSessionSet = new Stack<>();

    @Override
    public void start() {

        EventBus eventBus = vertx.eventBus();

        vertx.eventBus().consumer(MqttTopicConstant.STOP_ALL_CLIENT_TOPIC, this::stopAllHandler);

        String str = context.config().getString("configBean");

        final Config config = JSON.parseObject(str, Config.class);
        String host = config.getServer().getIp();
        int port = config.getServer().getPort();
        int interval = config.getInterval();
        // 成功失败次数在本地汇总，不在总线进行计算。总线10秒打印一次汇总结果
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger totalCount = new AtomicInteger(0);

        // 发送消息体次数统计
        // 连接成功后会发送login消息一次，会有返回值。
        AtomicInteger messageCount = new AtomicInteger(-1);
        List<SocketSessionBean> list = getSessionList();
        int totalConnection = list.size();
        log.info("total connection is {}", totalConnection);

        vertx.setPeriodic(interval, id -> {
            long currentTime = System.currentTimeMillis();
            NetClientOptions netClientOptions = initClientOptions();
            SocketSessionBean socketSessionBean = list.get(totalCount.get());
            NetClient client = vertx.createNetClient(netClientOptions);

            client.connect(port, host, s -> {
                MetricRateBean metricRateBean = MetricRateBean.builder().startTime(currentTime)
                        .endTime(System.currentTimeMillis()).successCount(0).totalCount(1).errorCount(0)
                        .countFinished(false).timeCost(System.currentTimeMillis() - currentTime).build();
                if (s.succeeded()) {
                    long endTime = System.currentTimeMillis();
                    log.info(
                            "ip {} client connected to a server success, current success count {}, total count {},"
                                    + " connection time cost is {} ms",
                            netClientOptions.getLocalAddress(), successCount.get(), totalCount.get(),
                            endTime - currentTime);
                    successCount.incrementAndGet();

                    Method.sendMessage(socketSessionBean.getLoginMessage(), s.result());

                    metricRateBean.setSuccessCount(1);
                    socketSessionSet.push(client);
                    NetSocket socket = s.result();
                    socket.closeHandler(t -> {
                        log.info("receive close message");
                    });
                    socket.exceptionHandler(event -> {
                        log.error("", event);
                    });
                    socket.handler(message -> {
                        log.info("receive message {}", message.toString());
                    });

                    socket.handler(message -> {
                        log.info("receive message {}", message.toString());
                        if (config.getMessage().isPubMessage()) {
                            Method.sendPubMessage(socketSessionBean.getPubMessage(), messageCount, config, socket, endTime);
                        }
                    });
                } else {
                    errorCount.incrementAndGet();
                    log.error("connection failed {}", s.cause().getMessage());
                    metricRateBean.setErrorCount(1);
                }
                String bean = null;
                // 防止连接总数到了，metric仍在打印的问题
                if (successCount.get() + errorCount.get() >= totalConnection) {
                    metricRateBean.setCountFinished(true);
                    log.info("all connection finished," + " total connections {}, success {}, " + "error {}",
                            totalCount, successCount, errorCount);
                }
                bean = JSON.toJSONString(metricRateBean);
                eventBus.publish(MqttTopicConstant.CONNECTION_TOPIC, bean);

            });
//            client.close(handler -> {
//                log.warn("receive close message");
//            });
            // 独立计数，client连接建立过慢时会导致多发连接请求
            totalCount.incrementAndGet();

            if (totalCount.get() >= totalConnection) {
                log.info("{} ,time is up, stop timer, current status: total: {}, success: {}, error:{}",
                        netClientOptions.getLocalAddress(), totalCount.get(), successCount.get(), errorCount.get());
                vertx.cancelTimer(id);
            }
        });

    }

    public void stopAllHandler(Message<String> message) {
        log.info("stop all clients size {}", socketSessionSet.size());

        // 断链速度过快会导致EMQ侧消费过慢
        vertx.setPeriodic(100, id -> {
            if (!socketSessionSet.empty()) {
                NetClient mqttClient = socketSessionSet.pop();
                mqttClient.close();
            } else {
                vertx.cancelTimer(id);
            }
        });
    }

    public List<SocketSessionBean> getSessionList() {
        String jsonArray = config().getString("sessionList");
        return JSON.parseArray(jsonArray, SocketSessionBean.class);
    }

    public NetClientOptions initClientOptions() {
        NetClientOptions netClientOptions = new NetClientOptions();
        String localIp = context.config().getString("localIp");
        netClientOptions.setLocalAddress(localIp);
        netClientOptions.setLogActivity(true);
        return netClientOptions;
    }
}
