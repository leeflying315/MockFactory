package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KafkaDemoClientVerticle extends AbstractVerticle {

    @Override
    public void start()  {
        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", "172.30.125.55:9092");
        kafkaConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("acks", "1");
        kafkaConfig.put("security.protocol","SSL");

        kafkaConfig.put("ssl.truststore.location","E:\\问题\\kafkaSSL\\client.truststore.jks");

        kafkaConfig.put("ssl.truststore.password","test1234");

        kafkaConfig.put("ssl.keystore.location","E:\\问题\\kafkaSSL\\client.keystore.jks");

        kafkaConfig.put("ssl.keystore.password","test1234");

        kafkaConfig.put("ssl.key.password","test1234");

        kafkaConfig.put("ssl.endpoint.identification.algorithm","");

        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, kafkaConfig);

        KafkaProducerRecord<String, String> record =
                KafkaProducerRecord.create("test", "hello");

        producer.write(record).onComplete(result -> {
            log.info("send info success {} ", result.succeeded());
        });
    }
}
