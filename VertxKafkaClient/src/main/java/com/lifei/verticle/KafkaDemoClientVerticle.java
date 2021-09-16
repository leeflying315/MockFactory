package com.lifei.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifei.bean.LocalKafkaConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KafkaDemoClientVerticle extends AbstractVerticle {

    static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start()  {
        String str = context.config().getString("configBean");
        final LocalKafkaConfig localKafkaConfig;
        LocalKafkaConfig localKafkaConfig1;
        try {
            localKafkaConfig1 = objectMapper.readValue(str, LocalKafkaConfig.class);
        } catch (JsonProcessingException e) {
            log.error("", e);
            localKafkaConfig1 = null;
        }

        localKafkaConfig = localKafkaConfig1;
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
        vertx.setPeriodic(localKafkaConfig.getInterval(), t -> {
//            LocalDateTime localDateTime = LocalDateTime.now();
//            String format5 = localDateTime.format(formatter2);
//            log.info("time is {}", format5);
//            String msg = "{\"action\":51,\"data_size\":2,\"device_key\":\"26Iu3F15DWZkn4K\",\"log_level\":\"Info\",\"message_id\":\"10014\",\"operation_by_id\":\"cu31prk2bjseikYC:26Iu3F15DWZkn4K\",\"operation_code\":\"$sys/cu31prk2bjseikYC/26Iu3F15DWZkn4K/property/batch\",\"product_key\":\"cu31prk2bjseikYC\",\"rawdata\":\"{\\\"messageId\\\":\\\"10014\\\",\\\"params\\\":{\\\"lightVoltage\\\":{\\\"value\\\":26.4,\\\"ts\\\":1626146567193},\\\"lightCurrent\\\":{\\\"value\\\":2,\\\"ts\\\":1626146567193}}}\",\"request_time\":\"" +
//                    format5+  "\",\"sequence_id\":\"0cd21ffb-321f-4e4c-be6d-caaf13b0e8e2\",\"service_type\":201,\"status_code\":\"000000\"}";
//            String msg = "{\"dataType\":\"0\",\"productKey\":\"cu31prk2bjseikYC\",\"productName\":\"测试物模型\",\"deviceKey\":\"26Iu3F15DWZkn4K\",\"deviceName\":\"auto26Iu3F15DWZkn4K\",\"orgId\":\"168\",\"orgName\":\"北京市分公司\",\"ruleExecuteTime\":\"2021-08-30 15:51:46.599\",\"ruleId\":\"666\",\"ruleName\":\"属性转发\",\"messageId\":\"10014\",\"properties\":\"[{\\\"device.prop.value\\\":26.4,\\\"device.prop.ts\\\":\\\"2021-08-30 15:51:46.541\\\",\\\"device.prop.name\\\":\\\"\\\",\\\"device.prop.key\\\":\\\"lightVoltage\\\"},{\\\"device.prop.value\\\":2,\\\"device.prop.ts\\\":\\\"2021-08-30 15:51:46.541\\\",\\\"device.prop.name\\\":\\\"\\\",\\\"device.prop.key\\\":\\\"lightCurrent\\\"}]\",\"dataTime\":\"2021-08-30 15:51:46.541\",\"orgKey\":\"CU_BEIJING\",\"recordMode\":\"1\",\"iotId\":\"ddab0a37630b4828b2a0ef6af21e2e02\",\"nodeType\":\"0\",\"createdBy\":\"cu31prk2bjseikYC:26Iu3F15DWZkn4K\",\"connectionProtocol\":\"0\",\"authType\":\"0\",\"IP\":null,\"updater\":null,\"updatedOn\":\"2021-08-30 15:51:46.541\",\"description\":\"\",\"activatedOn\":null,\"gatewayId\":\"\",\"imei\":\"\",\"iccid\":\"\",\"imsi\":\"\",\"sequenceId\":\"" +
//                    UUID.randomUUID()
//                    + "\"}";
//            String msg = "{\"action\":51,\"data_size\":1,\"device_key\":\"GGGGGB\",\"log_level\":\"Info\",\"message_id\":\"123435543\",\"operation_by_id\":\"cu6fls9hcrje5k1V:GGGGGB\",\"operation_code\":\"$sys/cu6fls9hcrje5k1V/GGGGGB/property/pub\",\"product_key\":\"cu6fls9hcrje5k1V\",\"rawdata\":\"{\\\"messageId\\\":\\\"123435543\\\",\\\"params\\\":{\\\"1212\\\":{\\\"value\\\":55,\\\"ts\\\":\\\"1631072544647\\\"}}}\",\"request_time\":\"2021-10-10 11:42:25.269\",\"sequence_id\":\"0ef61dcc-f314-40e3-acfa-833ebc5ef55a\",\"service_type\":201,\"status_code\":\"000000\"}";
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create(localKafkaConfig.getTopic(), localKafkaConfig.getMsg());
            producer.write(record).onComplete(result -> {
                log.info("send info success {} ", result.succeeded());
            });
        });
    }
}
