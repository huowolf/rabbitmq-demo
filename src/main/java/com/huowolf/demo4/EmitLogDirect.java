package com.huowolf.demo4;

import com.google.common.base.Strings;
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
public class EmitLogDirect {
    private final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ所在主机ip或者主机名
        factory.setHost("47.106.234.196");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个频道
        Channel channel = connection.createChannel();
        // 指定转发——广播
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //所有日志严重性级别
        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = severities[i%3];//每一次发送一条不同严重性的日志

            // 发送的消息
            String message = "Hello World"+ Strings.repeat(".", i+1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println(" [x] Sent '" + severity +"':'"+ message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}