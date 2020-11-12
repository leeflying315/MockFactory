package com.china.unicom.mqtt;

import com.china.unicom.mqtt.verticle.ServerPortTest;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class MqttServerStarter {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(MqttServerVerticle.class.getName(),
//                new DeploymentOptions().setInstances(16));
        vertx.deployVerticle(ServerPortTest.class.getName(),
                new DeploymentOptions().setInstances(1));
    }
}
