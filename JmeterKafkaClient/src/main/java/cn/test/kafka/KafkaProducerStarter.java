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

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/11/19
 */
public class KafkaProducerStarter extends AbstractJavaSamplerClient {
    public static KafkaProducer<String, String> producer;

    static {
        // 配置信息
        Properties props = new Properties();
        // kafka服务器地址

        // 测试环境
        props.put("bootstrap.servers", "172.30.208.108:9092");

        // 沙箱环境
//         props.put("bootstrap.servers", "172.30.208.65:9092,172.30.208.66:9092,172.30.208.67:9092");
//        props.put("bootstrap.servers", "172.30.125.52:9092");
        // 设置数据key和value的序列化处理类
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        // 创建生产者实例
        producer = new KafkaProducer<>(props);
    }

    @Override
    public Arguments getDefaultParameters() {

        Arguments args = new Arguments();
        args.addArgument("topic", "test");
        args.addArgument("message", "hello");
        return args;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String topic = arg0.getParameter("topic");
        String message = arg0.getParameter("message");

        // System.out.println(read_key.getAndIncrement());
        SampleResult results = new SampleResult();
        // System.out.println(context.getParameter("rw"));
        results.sampleStart();
        // 1、获取一个集合foo
        ResponseResult result = sendMsgToProcess(topic, processJson(message));
        if (!result.getResult()) {
            results.setSuccessful(false);
            results.setResponseCode("500");
        } else {
            results.setResponseCodeOK();
            results.setSuccessful(true);
        }
        results.setResponseMessage(Long.toString(result.getTimestamp()));
        results.sampleEnd();
        return results;
    }

    public static void main(String[] args) {
        KafkaProducerStarter kafkaProducerStarter = new KafkaProducerStarter();
        String result = kafkaProducerStarter.processJson("");
        System.out.println(result);
    }

    public static ResponseResult sendMsgToProcess(String kafkaTopic, String data) {
        ProducerRecord record = new ProducerRecord<String, String>(kafkaTopic, data);
        try {
            producer.send(record).get();
            ResponseResult responseResult = new ResponseResult();
            responseResult.setResult(true);
            responseResult.setTimestamp(System.currentTimeMillis());
            return responseResult;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseResult responseResult = new ResponseResult();
            responseResult.setResult(false);
            responseResult.setTimestamp(System.currentTimeMillis());
            return responseResult;
        }
    }

    private String processJson(String rawData) {
        JSONObject jsonObject = JSONObject.parseObject(rawData);
        Set<String> keys = jsonObject.keySet();
        JSONObject result = null;
        // 剥离掉第一层
        for (String key : keys) {
            Object value = jsonObject.get(key);
            JSONArray jsonArray = (JSONArray)value;
            // 数组中只有一个元素
            JSONObject jsonStr = (JSONObject)jsonArray.get(0);
            System.out.println(jsonStr);
            String messageId = UUID.randomUUID().toString().replaceAll("-", "");

            result = modifyMessageId(jsonStr, messageId);
        }
        return JSONObject.toJSONString(result);
    }

    public JSONObject modifyMessageId(JSONObject jsonObject, String messageId) {
        System.out.println(messageId);
        Map<String, Object> allKeyValue = getAllKeyValue(jsonObject);
        for (String key : allKeyValue.keySet()) {
            if ("message_id".equals(key)) {
                jsonObject.put("message_id", messageId);
            }
            if ("messageId".equals(key)) {
                jsonObject.put("messageId", messageId);
            }
            if ("cmd_data".equals(key) || "rawdata".equals(key) || "event_data".equals(key)) {
                String body = processBodyMessage((String)allKeyValue.get(key), messageId);
                jsonObject.put(key, body);
            }
        }
        return jsonObject;
    }

    private String processBodyMessage(String jsonString, String messageId) {
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
