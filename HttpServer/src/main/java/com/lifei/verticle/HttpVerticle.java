package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/9/23
 */
public class HttpVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(HttpVerticle.class);

    @Override
    public void start() {

        logger.info("start http server");
        HttpServer server = vertx.createHttpServer(init());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/ok").handler(this::normalEnd);
        router.route("/waiting").handler(this::waitHandler);
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

    public void normalEnd(RoutingContext routingContext) {
        logger.info("receive message {}", routingContext.getBodyAsString());
        routingContext.response().end("info received");
    }

    public void failedHandler(RoutingContext routingContext) {
        logger.info("receive message {}", routingContext.getBodyAsString());
        routingContext.response().setStatusCode(300).end("info received");
    }

    public void waitHandler(RoutingContext routingContext) {
        String waitingTime = routingContext.request().getParam("waiting");
        if (waitingTime == null) {
            waitingTime = "1000";
        }
        logger.info("receive message {}", routingContext.getBodyAsString());
        vertx.setTimer(Integer.parseInt(waitingTime), t -> {
            logger.info("send response");
            routingContext.response().end("info received");
        });
    }

    public HttpServerOptions init() {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setPort(8883);
        return httpServerOptions;
    }

}
