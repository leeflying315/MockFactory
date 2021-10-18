package com.lifei.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifei.bean.LocalKafkaConfig;
import com.lifei.utils.KafkaTopicConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KafkaConsumerVerticle extends AbstractVerticle {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    LocalKafkaConfig config;
    @Override
    public void start() {
        String str = context.config().getString("configBean");
        LocalKafkaConfig configTemp = null;
        try {
            configTemp = objectMapper.readValue(str, LocalKafkaConfig.class);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        final LocalKafkaConfig config = configTemp;

        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", config.getKafkaServer());
        kafkaConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("acks", "1");

        // use producer for interacting with Apache Kafka
        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, kafkaConfig);
        sendMessage(producer);
    }

    public void sendMessage(KafkaProducer<String, String> producer) {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(KafkaTopicConstant.PUBLISH_TOPIC, message -> publishMsgHandler(producer, message));
    }

    public void publishMsgHandler(KafkaProducer<String, String> producer, Message<Object> message) {
        String body = message.body().toString();
        log.info("receive msg from topic: {}", body);
        sendMessage(body, "rawdata", producer);
    }

    public void sendMessage(String data, String topic, KafkaProducer<String, String> producer) {
        try {
            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create(topic, data);

            producer.write(record).onComplete(result -> {
                log.info("send info success {} ", result.succeeded());
            });
        } catch (Exception exception) {
            log.error("send info exception", exception);
        }
    }
}
