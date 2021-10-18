package com.lifei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/9/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalKafkaConfig {
    Integer interval;

    String msg;

    Integer instance;

    String topic;

    Integer httpInstance;

    String kafkaServer;
}
