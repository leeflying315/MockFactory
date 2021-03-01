package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import lombok.extern.log4j.Log4j2;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/3/1
 */
@Log4j2
public class TcpSingleVerticle extends AbstractVerticle {
    @Override
    public void start() {
        NetClientOptions netClientOptions = new NetClientOptions().setLogActivity(true);
//                .setSsl(true)
//                .setTrustAll(true);;
        NetClient client = vertx.createNetClient(netClientOptions);
        client.connect(8844, "153.35.119.72", s -> {
            log.info("connected status is {}", s.succeeded());
            log.error("",s.cause());
        });
    }
}
