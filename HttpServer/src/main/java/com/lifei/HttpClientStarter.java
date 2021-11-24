package com.lifei;

import com.lifei.benchmark.ClientBenchMarkVerticle;
import io.vertx.core.Vertx;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/3/4
 */
public class HttpClientStarter {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ClientBenchMarkVerticle.class.getName());
    }
}
