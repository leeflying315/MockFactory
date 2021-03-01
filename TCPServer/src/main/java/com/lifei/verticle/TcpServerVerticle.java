package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import lombok.extern.log4j.Log4j2;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/2/20
 */
@Log4j2
public class TcpServerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        int port = 8190;
        log.info("start server on {}", port);
        NetServer server = vertx.createNetServer();

        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                log.info("I received some bytes: {}", buffer.toString());
                socket.write("hello world");
            });
            socket.closeHandler(v -> {
                log.info("The socket has been closed");
            });
        });

        server.listen(port, "0.0.0.0", res -> {
            if (res.succeeded()) {
                log.info("Server is now listening on {}", server.actualPort());
            } else {
                log.info("Failed to bind!");
            }
        });


    }
}
