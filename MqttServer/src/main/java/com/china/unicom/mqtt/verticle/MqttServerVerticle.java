package com.china.unicom.mqtt.verticle;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.messages.MqttPublishMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MqttServerVerticle extends AbstractVerticle {
    @Override
    public void start() {
        MqttServerOptions options = new MqttServerOptions().setPort(8883);
//            .setPfxKeyCertOptions(new PfxOptions().setPath("clientcert.p12").setPassword("123456")).setSsl(true);

        MqttServer mqttServer = MqttServer.create(vertx, options);

        mqttServer.endpointHandler(endpoint -> {

            // shows main connect info
            log.info("receive connection from {}, clientId {}", endpoint.auth().getUsername(),
                endpoint.clientIdentifier());
            // accept connection from the remote client
            endpoint.accept(false);
            endpoint.closeHandler(event -> {
                log.info("session closed {}", endpoint.clientIdentifier());
            });
            endpoint.exceptionHandler(event -> {
                log.error("", event);
            });
            endpoint.publishHandler(event -> publishHandler(event, endpoint));

        }).listen(ar -> {

            if (ar.succeeded()) {
                log.info("MQTT server is listening on port {}", mqttServer.actualPort());
            } else {
                log.error("Error on starting the server {}", ar.cause().getMessage());
            }
        });
    }

    private void publishHandler(MqttPublishMessage mqttPublishMessage, MqttEndpoint endpoint) {
        String message = mqttPublishMessage.payload().toString();
        if (mqttPublishMessage.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
            endpoint.publishAcknowledge(mqttPublishMessage.messageId());
        } else if (mqttPublishMessage.qosLevel() == MqttQoS.EXACTLY_ONCE) {
            endpoint.publishReceived(mqttPublishMessage.messageId());
        }
        log.info("topic is {}, message is {}", mqttPublishMessage.topicName(), message);
    }
}
