package com.lifei;

import com.lifei.verticle.TcpSingleVerticle;
import io.vertx.core.Vertx;

/**
 * @Author lifei
 * @Description: TCP 单一client启动器
 * @Date 2021/3/1
 */
public class TcpSingleClientStarter {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(TcpSingleVerticle.class.getName());
    }
}
