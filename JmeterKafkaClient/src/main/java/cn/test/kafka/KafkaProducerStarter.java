package cn.test.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/11/19
 */
public class KafkaProducerStarter extends AbstractJavaSamplerClient {

    public static final int producerNum = 50;//实例池大小
    public static BlockingQueue<KafkaProducer<String, String>> queue = new LinkedBlockingQueue<>(producerNum);
    public static int total = 0;

    static {

        // 创建生产者实例
        for (int i = 0; i < producerNum; i++) {
            KafkaProducer<String, String> producer = new KafkaProducer<>(initProperties());
            queue.add(producer);
        }
    }

    public static Properties initProperties() {
        // 配置信息
        Properties props = new Properties();
        // kafka服务器地址

        // 测试环境
//        props.put("bootstrap.servers", "172.30.208.108:9092");

        // 沙箱环境
        props.put("bootstrap.servers", "172.30.208.65:9092,172.30.208.66:9092,172.30.208.67:9092");
        // 开发环境
//        props.put("bootstrap.servers", "172.30.125.52:9092");
        // 设置数据key和value的序列化处理类
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("acks", "1");//有0,1，all三种形式

        props.put("batch.size", 524288);
        props.put("linger.ms", 50);
        props.put("buffer.memory", 33554432);//32M

        return props;
    }

    @Override
    public Arguments getDefaultParameters() {

        Arguments args = new Arguments();
        args.addArgument("topic", "test");
        args.addArgument("message1", "hello");
        args.addArgument("message2", "hello");
        args.addArgument("message3", "hello");
        args.addArgument("message4", "hello");
        args.addArgument("message5", "hello");
        args.addArgument("message6", "hello");
        args.addArgument("message7", "hello");
        args.addArgument("message8", "hello");
        args.addArgument("message9", "hello");
        args.addArgument("message10", "hello");

        args.addArgument("send", "1000");
        return args;
    }

    public Arguments localTestParams() {

        Arguments args = new Arguments();
        args.addArgument("topic", "test");
        args.addArgument("message2", "hello");
        args.addArgument("message3", "hello");
        args.addArgument("message4", "hello");
        args.addArgument("message5", "hello");
        args.addArgument("message6", "hello");
        args.addArgument("message7", "hello");
        args.addArgument("message8", "hello");
        args.addArgument("message9", "hello");
        args.addArgument("message10", "hello");

        args.addArgument("send", "1000");
        return args;
    }
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String topic = arg0.getParameter("topic");
        String message1 = arg0.getParameter("message1");
        String message2 = arg0.getParameter("message2");
        String message3 = arg0.getParameter("message3");
        String message4 = arg0.getParameter("message4");
        String message5 = arg0.getParameter("message5");
        String message6 = arg0.getParameter("message6");
        String message7 = arg0.getParameter("message7");
        String message8 = arg0.getParameter("message8");
        String message9 = arg0.getParameter("message9");
        String message10 = arg0.getParameter("message10");

        int counts = Integer.parseInt(arg0.getParameter("send"));
        // System.out.println(read_key.getAndIncrement());
        SampleResult results = new SampleResult();
        // System.out.println(context.getParameter("rw"));
        results.sampleStart();
        // 1、获取一个集合foo
        for (int i = 0; i < counts; i++) {
            sendMsgToProcess(topic, message1);
            sendMsgToProcess(topic, message2);
            sendMsgToProcess(topic, message3);
            sendMsgToProcess(topic, message4);
            sendMsgToProcess(topic, message5);
            sendMsgToProcess(topic, message6);
            sendMsgToProcess(topic, message7);
            sendMsgToProcess(topic, message8);
            sendMsgToProcess(topic, message9);
            sendMsgToProcess(topic, message10);
        }

        results.setResponseCodeOK();
        results.setSuccessful(true);
        results.setResponseMessage(total + "");
        results.sampleEnd();
        return results;


    }

    public static void main(String[] args) {
        KafkaProducerStarter kafkaProducerStarter = new KafkaProducerStarter();
        Arguments arg0 = kafkaProducerStarter.localTestParams();
        arg0.addArgument("message1", "{\"ods_device_cmd\":[{\"product_key\":\"cu3dbb1lo517s7wP\",\"device_key\":\"8GwJGnCrywcH2UH\",\"data_time\":\"1970-01-01 08:00:00.000\",\"request_time\":\"2020-12-15 18:28:05.196\",\"request_ip\":\"172.30.225.185\",\"operation_code\":\"$sys\\/cu3dbb1lo517s7wP\\/8GwJGnCrywcH2UH\\/service\\/pub\",\"operation_name\":null,\"operation_by_id\":\"cu3dbb1lo517s7wP&8GwJGnCrywcH2UH\",\"operation_by_name\":null,\"service_type\":202,\"log_level\":\"Info\",\"status_code\":\"000000\",\"status_msg\":null,\"message_id\":\"0b03b3576b6743c2b080f1c3e03062c6\",\"action\":21,\"cmd_key\":\"deleteVehicleInfo\",\"cmd_name\":\"删除车辆信息\",\"cmd_data\":\"{\\\"userId\\\":\\\"2345\\\",\\\"carNumber\\\":\\\"112345\\\"}\",\"insert_time\":\"2020-12-15T10:28:05.000Z\"}]}");
        JavaSamplerContext javaSamplerContext = new JavaSamplerContext(arg0);
        kafkaProducerStarter.runTest(javaSamplerContext);
//        String result = kafkaProducerStarter.processJson();
//        System.out.println(result);
    }

    // 发送消息到kafka
    public static void sendMsgToProcess(String kafkaTopic, String data) {
        if (data.equals("hello")) {
//            System.out.println(data);
            return;
        }
        String message = processJson(data);
        ProducerRecord record = new ProducerRecord<String, String>(kafkaTopic, message);
        try {
            KafkaProducer<String, String> kafkaProducer = queue.take();//从实例池获取连接,没有空闲连接则阻塞等待
            kafkaProducer.send(record).get();
            queue.put(kafkaProducer);//归还kafka连接到连接池队列
            total++;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    private static String processJson(String rawData) {
        JSONObject jsonObject = JSONObject.parseObject(rawData);
        Set<String> keys = jsonObject.keySet();
        JSONObject result = null;
        // 剥离掉第一层
        for (String key : keys) {
            Object value = jsonObject.get(key);
            JSONArray jsonArray = (JSONArray) value;
            // 数组中只有一个元素
            JSONObject jsonStr = (JSONObject) jsonArray.get(0);
            String messageId = UUID.randomUUID().toString().replaceAll("-", "");

            result = modifyMessageId(jsonStr, messageId);
        }
        return JSONObject.toJSONString(result);
    }

    public static JSONObject modifyMessageId(JSONObject jsonObject, String messageId) {
        Map<String, Object> allKeyValue = getAllKeyValue(jsonObject);
        for (String key : allKeyValue.keySet()) {
            if ("message_id".equals(key)) {
                jsonObject.put("message_id", messageId);
            }
            if ("messageId".equals(key)) {
                jsonObject.put("messageId", messageId);
            }
            if ("cmd_data".equals(key) || "rawdata".equals(key)) {
                String body = processBodyMessage((String) allKeyValue.get(key), messageId);
                jsonObject.put(key, body);
            }
        }
        return jsonObject;
    }

    private static String processBodyMessage(String jsonString, String messageId) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Map<String, Object> allKeyValue = getAllKeyValue(jsonObject);
        for (String key : allKeyValue.keySet()) {
            if ("message_id".equals(key)) {
                jsonObject.put("message_id", messageId);
            }
            if ("messageId".equals(key)) {
                jsonObject.put("messageId", messageId);
            }
        }
        return JSONObject.toJSONString(jsonObject);
    }

    // 公共属性中存在为数字类型
    public static Map<String, Object> getAllKeyValue(JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        Map<String, Object> list = new HashMap<>();
        for (String key : keys) {
            Object value = jsonObject.get(key);
            list.put(key, value);
        }
        return list;
    }
}
