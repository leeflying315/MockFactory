package com.china.unicom.mqtt.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/10/13
 */
public class HttpClientVerticle extends AbstractVerticle {
    @Override
    public void start(){
        WebClientOptions options = new WebClientOptions()
                .setUserAgent("My-App/1.2.3");
        options.setKeepAlive(false);

        WebClient client = WebClient.create(vertx, options);

    }
}
