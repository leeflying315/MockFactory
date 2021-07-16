package com.lifei;

import com.lifei.verticle.KafkaDemoClientVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LocalKafkaClientStarter {

    public static void main(String[] args) {


        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(KafkaDemoClientVerticle.class.getName(),  new DeploymentOptions().setInstances(2));
    }
}