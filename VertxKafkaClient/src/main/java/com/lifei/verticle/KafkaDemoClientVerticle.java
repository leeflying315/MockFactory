package com.lifei.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KafkaDemoClientVerticle extends AbstractVerticle {

    static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

//        producer.write(record).onComplete(result -> {
//            log.info("send info success {} ", result.succeeded());
//        });
        vertx.setPeriodic(10, t -> {
            LocalDateTime localDateTime = LocalDateTime.now();
            String format5 = localDateTime.format(formatter2);
            log.info("time is {}", format5);
            String msg = "{\"action\":51,\"data_size\":2,\"device_key\":\"26Iu3F15DWZkn4K\",\"log_level\":\"Info\",\"message_id\":\"10014\",\"operation_by_id\":\"cu31prk2bjseikYC:26Iu3F15DWZkn4K\",\"operation_code\":\"$sys/cu31prk2bjseikYC/26Iu3F15DWZkn4K/property/batch\",\"product_key\":\"cu31prk2bjseikYC\",\"rawdata\":\"{\\\"messageId\\\":\\\"10014\\\",\\\"params\\\":{\\\"lightVoltage\\\":{\\\"value\\\":26.4,\\\"ts\\\":1626146567193},\\\"lightCurrent\\\":{\\\"value\\\":2,\\\"ts\\\":1626146567193}}}\",\"request_time\":\"" +
                    format5+  "\",\"sequence_id\":\"0cd21ffb-321f-4e4c-be6d-caaf13b0e8e2\",\"service_type\":201,\"status_code\":\"000000\"}";
            log.info("msg is {}", msg);
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create("ck.device.rawdata", msg);

            producer.write(record).onComplete(result -> {
                log.info("send info success {} ", result.succeeded());
            });
        });
    }
}
