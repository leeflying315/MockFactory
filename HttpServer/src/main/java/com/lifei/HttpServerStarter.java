package com.lifei;

import com.lifei.verticle.HttpVerticle;
import com.lifei.verticle.HttpsAuthVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/9/23
 */
@Log4j2
public class HttpServerStarter {
    private static final Logger logger = LogManager.getLogger(HttpVerticle.class);

    public static void main(String[] args) {
        logger.info("server start");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpVerticle.class.getName(),
                new DeploymentOptions().setInstances(2));
//        vertx.deployVerticle(.class.getName(),
//                new DeploymentOptions().setInstances(2));
        vertx.deployVerticle(HttpsAuthVerticle.class.getName(),
                new DeploymentOptions().setInstances(2));
    }
}
