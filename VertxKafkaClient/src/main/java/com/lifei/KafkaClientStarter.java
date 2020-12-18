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
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage1());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage2() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage2());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage3() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage3());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage4() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage4());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage5() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage5());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage6() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage6());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage7() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage7());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage8() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage8());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage9() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage9());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
        }
        if (configBean.getMessage10() != null) {
            JsonObject config = new JsonObject().put("configBean", str).put("data", configBean.getMessage10());
            vertx.deployVerticle(KafkaClientVerticle.class.getName(), new DeploymentOptions().setConfig(config));
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
        initData(config, message1, message2, message3, message4, message5);
        initData(config, message6, message7, message8, message9, message10);

        return config;
    }

    private static void initData(Config config, String message6, String message7, String message8, String message9, String message10) {
        if (message6 != null && message6.length() != 0)
            config.setMessage1(message6);
        if (message7 != null && message7.length() != 0)
            config.setMessage1(message7);
        if (message8 != null && message8.length() != 0)
            config.setMessage1(message8);
        if (message9 != null && message9.length() != 0)
            config.setMessage1(message9);
        if (message10 != null && message10.length() != 0)
            config.setMessage1(message10);
    }
}
