package com.lifei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifei.bean.Config;
import com.lifei.verticle.KafkaClientVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;


@Slf4j
public class KafkaClientStarter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        log.info("test start");
//        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
        Config configBean = null;
        try {
            configBean = initConfig();
        } catch (IOException e) {
            log.error("", e);
        }
        log.info("config is {}", configBean);
        String str = "";
        try {
            str = objectMapper.writeValueAsString(configBean);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }


        Vertx vertx = Vertx.vertx();

        if (configBean.getMessage1() != null) {
            log.info("send Message1 start {} ", configBean.getMessage1());
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage1());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage2() != null) {
            log.info("send Message2 start {} ", configBean.getMessage2());
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage2());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage3() != null) {
            log.info("send Message3 start {} ", configBean.getMessage3());
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage3());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage4() != null) {
            log.info("send Message4 start {} ", configBean.getMessage4());
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage4());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage5() != null) {
            log.info("send Message5 start {} ", configBean.getMessage5());
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage5());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage6() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage6());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage7() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage7());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage8() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage8());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage9() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage9());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
        if (configBean.getMessage10() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage10());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config).setWorker(true));
        }
    }

    public static Config initConfig() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        String in = new String(readAllBytes(Paths.get("./conf/config.yaml")));

        // 本地调试使用
//        InputStream in = KafkaClientStarter.class.getResourceAsStream("/conf/config.yaml");
        Config config = yaml.loadAs(in, Config.class);

        String message1;
        try {
            message1 = new String(readAllBytes(Paths.get("./conf/message1.json")));
        } catch (Exception ex) {
            message1 = null;
        }
        String message2;
        try {
            message2 = new String(readAllBytes(Paths.get("./conf/message2.json")));
        } catch (Exception ex) {
            message2 = null;
        }
        String message3;
        try {
            message3 = new String(readAllBytes(Paths.get("./conf/message3.json")));
        } catch (Exception ex) {
            message3 = null;
        }
        String message4;
        try {
            message4 = new String(readAllBytes(Paths.get("./conf/message4.json")));
        } catch (Exception ex) {
            message4 = null;
        }
        String message5;
        try {
            message5 = new String(readAllBytes(Paths.get("./conf/message5.json")));
        } catch (Exception ex) {
            message5 = null;
        }
        String message6;
        try {
            message6 = new String(readAllBytes(Paths.get("./conf/message6.json")));
        } catch (Exception ex) {
            message6 = null;
        }
        String message7;
        try {
            message7 = new String(readAllBytes(Paths.get("./conf/message7.json")));
        } catch (Exception ex) {
            message7 = null;
        }
        String message8;
        try {
            message8 = new String(readAllBytes(Paths.get("./conf/message8.json")));
        } catch (Exception ex) {
            message8 = null;
        }
        String message9;
        try {
            message9 = new String(readAllBytes(Paths.get("./conf/message9.json")));
        } catch (Exception ex) {
            message9 = null;
        }
        String message10;
        try {
            message10 = new String(readAllBytes(Paths.get("./conf/message10.json")));
        } catch (Exception ex) {
            message10 = null;
        }
        initData01(config, message1, message2, message3, message4, message5);
        initData02(config, message6, message7, message8, message9, message10);

        return config;
    }

    private static void initData01(Config config, String message1, String message2, String message3, String message4, String message5) {
        if (message1 != null && message1.length() != 0)
            config.setMessage1(message1);
        if (message2 != null && message2.length() != 0)
            config.setMessage2(message2);
        if (message3 != null && message3.length() != 0)
            config.setMessage3(message3);
        if (message4 != null && message4.length() != 0)
            config.setMessage4(message4);
        if (message5 != null && message5.length() != 0)
            config.setMessage5(message5);
    }

    private static void initData02(Config config, String message1, String message2, String message3, String message4, String message5) {
        if (message1 != null && message1.length() != 0)
            config.setMessage6(message1);
        if (message2 != null && message2.length() != 0)
            config.setMessage7(message2);
        if (message3 != null && message3.length() != 0)
            config.setMessage8(message3);
        if (message4 != null && message4.length() != 0)
            config.setMessage9(message4);
        if (message5 != null && message5.length() != 0)
            config.setMessage10(message5);
    }
}