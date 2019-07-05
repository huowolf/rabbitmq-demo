package com.huowolf.demo2;

import com.google.common.base.Strings;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
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
        // 指定一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        int prefetchCount = 1;
        //限制发给同一个消费者不得超过1条消息
        channel.basicQos(prefetchCount);
        for (int i = 0; i < 5; i++) {
            // 发送的消息
            String message = "Hello World"+Strings.repeat(".",5-i)+(5-i);
            // 往队列中发出一条消息
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
