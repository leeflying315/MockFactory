package com.lifei;

import com.alibaba.fastjson.JSON;
import com.lifei.bean.Config;
import com.lifei.bean.SocketSessionBean;
import com.lifei.utils.Utils;
import com.lifei.verticle.MetricVerticle;
import com.lifei.verticle.TcpBenchMarkVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.readAllBytes;

/**
 * @Author lifei
 * @Description: TCP应用启动器
 * @Date 2021/2/19
 */
@Log4j2
public class TcpClientStarter {
    public static void main(String[] args) {

        Config configBean = null;
        try {
            configBean = initConfig();
        } catch (IOException e) {
            log.error("", e);
        }
        if (configBean == null) {
            log.error(" read input params failed");
            return;
        }
        JsonObject config = new JsonObject().put("configBean", JSON.toJSONString(configBean));

        String src = "conf/UPCT.txt";
        if (configBean.ipLists == null) {
            log.error(" no source ip input in config.yaml, system exit");
            return;
        }
        String[] sourceIps = configBean.getIpLists().split(",");
        List<List<SocketSessionBean>> sortSessionGroup = getSessionInfo(src, configBean.getTotalConnection(), sourceIps.length);

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(MetricVerticle.class.getName(), new DeploymentOptions().setConfig(new JsonObject()
                .put("instance", sortSessionGroup.size())));
        int currentIps = 0;
        for (List<SocketSessionBean> sessionBeanList : sortSessionGroup) {
            log.info("verticle start by ip {}", sourceIps[currentIps]);
            config.put("localIp", sourceIps[currentIps]);
            String jsonArray = JSON.toJSONString(sessionBeanList);
            config.put("sessionList", jsonArray);
            // 定时建连
            vertx.deployVerticle(TcpBenchMarkVerticle.class.getName(),
                    new DeploymentOptions().setConfig(config));
            currentIps++;
        }

    }

    public static Config initConfig() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        String input = new String(readAllBytes(Paths.get("./conf/config.yaml")));
        return yaml.load(input);
    }

    public static List<List<SocketSessionBean>> getSessionInfo(String src, int totalConnection, int networkCards) {
        List<SocketSessionBean> socketSessionBeanList = Utils.readCSVFileData(src);
        if (socketSessionBeanList.size() < totalConnection) {
            log.error("input mock session data counts {}, less than target {}", socketSessionBeanList.size(),
                    totalConnection);
        }
        socketSessionBeanList = socketSessionBeanList.subList(0, totalConnection);
        return Utils.splitList(socketSessionBeanList, networkCards);

    }
}
