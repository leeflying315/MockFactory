package com.lifei;

import com.lifei.client.ClientVerticle;
import io.vertx.core.Vertx;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/3/4
 */
public class HttpClientStarter {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ClientVerticle.class.getName());
    }
}
