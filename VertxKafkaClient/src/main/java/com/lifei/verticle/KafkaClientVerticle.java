package com.lifei.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifei.bean.Config;
import com.lifei.utils.KafkaProduceUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KafkaClientVerticle extends AbstractVerticle {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start() throws InterruptedException {
        String str = context.config().getString("configBean");
        Config configTemp = null;
        try {
            configTemp = objectMapper.readValue(str, Config.class);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        final Config config = configTemp;

        String data = context.config().getString("data");


        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", config.getKafkaServer());
        kafkaConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put("acks", "1");

        // use producer for interacting with Apache Kafka
        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, kafkaConfig);
        AtomicInteger costTime = new AtomicInteger(0);
        // 每秒发送次数
        vertx.setPeriodic(1000, t -> {
            sendMessage(config.getCounts(), data, config.getTopic(), producer);
            int nowCounts = costTime.addAndGet(1);
            if (nowCounts > config.getDuration()) {
                log.info("send finish, total send {} message", config.getCounts() * config.getDuration());
                vertx.cancelTimer(t);
            }
        });

    }

    public void sendMessage(int counts, String data, String topic, KafkaProducer<String, String> producer) {
        for (int i = 0; i < counts; i++) {
            try {
                String message = KafkaProduceUtils.processJson(data);
                // only topic and message value are specified, round robin on destination partitions
                KafkaProducerRecord<String, String> record =
                        KafkaProducerRecord.create(topic, message);

                producer.write(record).onComplete(result -> {
                    log.info("send info success {} ", result.succeeded());
                });
            } catch (Exception exception) {
                log.error("send info exception", exception);
            }
        }

    }
}
