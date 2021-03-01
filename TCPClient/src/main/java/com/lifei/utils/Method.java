package com.lifei.utils;

import com.lifei.bean.Config;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author lifei
 * @Description: 发布登陆消息
 * @Date 2021/2/19
 */
@Log4j2
public class Method {
    public static void sendMessage(String loginMessage, NetSocket netSocket) {
        Buffer buff = Buffer.buffer();

        buff.appendString(EnCode.fromHex(loginMessage));
        netSocket.write(buff, r -> {
            log.info("send {} result is {}", buff.toString(), r.succeeded());
        });
    }

    public static void sendPubMessage(String pubMessage, AtomicInteger count, Config config, NetSocket netSocket, long endTime) {
        count.incrementAndGet();
        if (count.get() < config.getMessage().getMessageCount()) {
            Buffer buff = Buffer.buffer();
            buff.appendString(EnCode.fromHex(pubMessage));
            netSocket.write(buff, r -> {
                log.info("send {} result is {}", buff.toString(), r.succeeded());
            });
        } else {
            long timeCost = System.currentTimeMillis() - endTime;
            log.info("total send message {}, total cost {}ms, speed {} ms/s", count.get(), timeCost, timeCost / count.get());
        }
    }
}
