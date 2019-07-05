package com.huowolf.demo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huowolf
 * @date 2019/7/5
 * @description
 */
public class EmitLog {
    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ所在主机ip或者主机名
        factory.setHost("47.106.234.196");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个频道
        Channel channel = connection.createChannel();
        // 指定转发——广播
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        for(int i=0;i<6;i++){
            // 发送的消息
            String message = "Hello World!";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}