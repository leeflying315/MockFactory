package com.lifei.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.net.PemKeyCertOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/3/4
 */
public class ClientVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(ClientVerticle.class);


    @Override
    public void start() {
        HttpClientOptions options = new HttpClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem")).setVerifyHost(false);
        HttpClient client = vertx.createHttpClient(options);
        client.request(HttpMethod.POST, 8885, "172.30.125.55", "/ok")
                .compose(req -> req.send()
                        .compose(HttpClientResponse::body))
                .onSuccess(body -> logger.info("Got data " + body.toString("ISO-8859-1")))
                .onFailure(err -> {
                    logger.error("", err);
                });

    }
}
