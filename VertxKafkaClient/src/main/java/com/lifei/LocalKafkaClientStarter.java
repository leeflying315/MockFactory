package com.lifei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifei.bean.LocalKafkaConfig;
import com.lifei.verticle.KafkaDemoClientVerticle;
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
public class LocalKafkaClientStarter {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {

        LocalKafkaConfig configBean = null;
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
        JsonObject config = new JsonObject().put("configBean", str);

        vertx.deployVerticle(KafkaDemoClientVerticle.class.getName(),  new DeploymentOptions().setInstances(configBean.getInstance()).setConfig(config));
    }

    public static LocalKafkaConfig initConfig() throws IOException {
        Yaml yaml = new Yaml(new Constructor(LocalKafkaConfig.class));
        String in = new String(readAllBytes(Paths.get("./conf/local_kafka_config.yaml")));

        return yaml.loadAs(in, LocalKafkaConfig.class);
    }
}