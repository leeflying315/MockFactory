package com.lifei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/2/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    public Server server;

    // 总连接数
    private Integer totalConnection;
    // 单个实例的连接间隔
    public Integer interval;

    // 源IP列表，使用逗号分隔
    public String ipLists;

    public Message message;

    @Data
    public static class Server {
        public Integer port;

        public String ip;
    }

    @Data
    public static class Message {

        public Integer messageCount;

        public boolean pubMessage;
    }
}
