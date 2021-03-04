package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/9/24
 */
public class HttpsAuthVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(HttpVerticle.class);

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer(init());
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/ok").handler(HttpsAuthVerticle::normalEnd);
        router.route("/waiting/30").handler(this::waitHandler);
        router.route("/failed").handler(this::failedHandler);

        router.route().failureHandler(route -> {
            logger.error("Routing error:", route.failure());
            int code = 500;
            route.response().setStatusCode(code).end();
        });
        server.requestHandler(router).listen(response -> {
            if (response.succeeded()) {
                logger.info("server listen on {}", server.actualPort());
            } else {
                logger.error("", response.cause());
            }
        });
    }

    public static void normalEnd(RoutingContext routingContext) {
        logger.info("receive message {}", routingContext.getBodyAsString());
        routingContext.response().end("info received");
    }

    public void failedHandler(RoutingContext routingContext) {
        logger.info("receive message {}", routingContext.getBodyAsString());
        routingContext.response().setStatusCode(300).end("info received");
    }

    public void waitHandler(RoutingContext routingContext) {
        logger.info("receive message {}", routingContext.getBodyAsString());
        vertx.setTimer(30000, t -> {
            logger.info("send response");
            routingContext.response().end("info received");
        });
    }

    public HttpServerOptions init() {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setPort(8885);
        httpServerOptions.setSsl(true);
        httpServerOptions
            .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem"));
//        httpServerOptions.setClientAuth(ClientAuth.NONE);
        httpServerOptions.setClientAuth(ClientAuth.REQUIRED)
            .setPemTrustOptions(new PemTrustOptions().addCertPath("server-cert.pem"));
//         .setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble"));
        return httpServerOptions;
    }
}
