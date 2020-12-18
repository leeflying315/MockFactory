package com.lifei.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/11/19
 */
public class KafkaProduceUtils {


    public static String processJson(String rawData) {
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
