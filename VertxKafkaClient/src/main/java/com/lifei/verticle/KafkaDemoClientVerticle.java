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
        kafkaConfig.put("bootstrap.servers", "172.30.125.52:9092");
        kafkaConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("acks", "1");
//        kafkaConfig.put("security.protocol","SSL");

//        kafkaConfig.put("ssl.truststore.location","E:\\问题\\kafkaSSL\\client.truststore.jks");

//        kafkaConfig.put("ssl.truststore.password","test1234");

//        kafkaConfig.put("ssl.keystore.location","E:\\问题\\kafkaSSL\\client.keystore.jks");

//        kafkaConfig.put("ssl.keystore.password","test1234");

//        kafkaConfig.put("ssl.key.password","test1234");

        kafkaConfig.put("ssl.endpoint.identification.algorithm","");

        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, kafkaConfig);

        // lwm2m开发环境测试消息
//        KafkaProducerRecord<String, String> record =
//                KafkaProducerRecord.create("lwm2m.passthrough", "{\"deviceKey\":\"Po6rnsklLRAyUXi\",\"lwm2mTopic\":\"lwm2m/000000019940124/up/notify\",\"payLoad\":\"test\",\"productKey\":\"cu31prk2bjseikYC\"}");

        KafkaProducerRecord<String, String> record =
                KafkaProducerRecord.create("ck.device.rawdata", "{\"action\":51,\"device_key\":\"thermostat01\",\"log_level\":\"Info\",\"message_id\":\"16061003333422\",\"operation_by_id\":\"cu6kq33bu365mhYn&pad_003\",\"operation_code\":\"$sys/cu6kq33bu365mhYn/pad_003/property/pub\",\"product_key\":\"cu23o9t2xsyllwnC\",\"rawdata\":\"{\\\"messageId\\\":\\\"16061003333422\\\",\\\"params\\\":{\\\"currentTemperature\\\":{\\\"value\\\":10,\\\"ts\\\":\\\"1606100317842\\\"},\\\"currentElectric\\\":{\\\"value\\\":0.57,\\\"ts\\\":\\\"1606100317842\\\"}}}\",\"request_time\":\"2022-11-23 10:58:49.690\",\"service_type\":201,\"status_code\":\"000000\"}");
//        producer.write(record).onComplete(result -> {
//            log.info("send info success {} ", result.succeeded());
//        });
        vertx.setPeriodic(10, t -> {
            producer.write(record).onComplete(result -> {
                log.info("send info success {} ", result.succeeded());
            });
        });
    }
}
