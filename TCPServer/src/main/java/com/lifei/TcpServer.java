package com.lifei;

import com.lifei.verticle.TcpServerVerticle;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/2/20
 */
@Log4j2
public class TcpServer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        log.info("start server");
        vertx.deployVerticle(TcpServerVerticle.class.getName());
    }
}
