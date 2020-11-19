package cn.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @description:
 * @author: yun lu
 * @date: 2020/10/12
 **/

public class KafkaUtil {
    public KafkaProducer<String, String> producer;
    private static final KafkaUtil instance = new KafkaUtil();

    public KafkaUtil() {
        // 配置信息
        Properties props = new Properties();
        // kafka服务器地址
        // 沙箱环境
        // props.put("bootstrap.servers", "172.30.208.65:9092,172.30.208.66:9092,172.30.208.67:9092");
        props.put("bootstrap.servers", "172.30.125.52:9092");
        // 设置数据key和value的序列化处理类
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        // 创建生产者实例
        producer = new KafkaProducer<>(props);
    }

    public static KafkaUtil getInstance() {
        return instance;
    }

    public boolean sendMsgToProcess(String kafkaTopic, String data) {
        ProducerRecord record = new ProducerRecord<String, String>(kafkaTopic, data);
        System.out.println("topic is "+ kafkaTopic);
        System.out.println("data is " + data);
        try {
            producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 发送记录
        return true;
    }
}
