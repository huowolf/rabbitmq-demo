package com.huowolf.demo5;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huowolf
 * @date 2019/7/5
 * @description
 */
public class ReceiveLogs2Console {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.106.234.196");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        String[] routingKeys ={"auth.*","*.info","#.warning"};//关注所有的授权日志、所有info和waring级别的日志
        for (String routingKey : routingKeys) {
            //关注所有级别的日志（多重绑定）
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received ["  + envelope.getRoutingKey() + "] :'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}