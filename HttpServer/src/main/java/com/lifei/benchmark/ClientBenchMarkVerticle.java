package com.lifei.benchmark;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/3/4
 */
public class ClientBenchMarkVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(ClientBenchMarkVerticle.class);


    @Override
    public void start() {
        HttpClientOptions options = new HttpClientOptions();
//                .setSsl(true)
//                .setTrustAll(true)
//                .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem")).setVerifyHost(false);
        String requestBody = "{\"instanceId\":\"test\",\"redisObjList\":[{\"redisContext\":\"456\",\"redisKey\":\"789\"}]}";
        HttpClient client = vertx.createHttpClient(options);

        vertx.setPeriodic(20, t -> {
            sendRequest(client, requestBody);
        });


    }

    private void sendRequest(HttpClient client, String requestBody    ) {
        client.request(HttpMethod.POST, 8450, "172.30.125.52", "/openFeign/v1/cloud-to-edge/sync-with-instance")
                .compose(req -> {

                    MultiMap headers = req.headers();
                    headers.set("content-type", "application/json");

                    return req.send(requestBody)
                            .compose(HttpClientResponse::body)
                            ;
                })
                .onSuccess(body -> logger.info("Got data " + body.toString()))
                .onFailure(err -> {
                    logger.error("", err);
                });
    }
}
