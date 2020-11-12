package com.china.unicom.mqtt.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import lombok.extern.log4j.Log4j2;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/10/13
 */
@Log4j2
public class ServerPortTest extends AbstractVerticle {
    @Override
    public void start() {
        for (int i = 1; i < 60000; i = i + 2) {
            final int port = i;
            HttpServer server = vertx.createHttpServer().requestHandler(req -> {
                req.response().putHeader("content-type", "text/plain").end("Hello from Vert.x!");
            });
            // Now bind the server:
            server.listen(port,"172.30.208.87", res -> {
                if (res.succeeded()) {
                    log.info("server listen on {}", server.actualPort());
                } else {
                    log.error("failed", res.cause());
                }
            });
        }
    }
}
