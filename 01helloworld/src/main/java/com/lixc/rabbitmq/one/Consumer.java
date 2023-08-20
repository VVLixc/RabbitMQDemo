package com.lixc.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * com.lixc.rabbitmq.one
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 17:07
 */
public class Consumer {
    public static final String QUEUE_NAME = "Hello World";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.222.132");
        connectionFactory.setUsername("lixc");
        connectionFactory.setPassword("001214");
        Channel channel = connectionFactory.newConnection().createChannel();
        System.out.println("等待接收消息....");

        // 推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            String messageBody = new String(message.getBody());//消息分为消息头、消息体；这里获取消息体
            System.out.println( messageBody);
        };

        // 取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> System.out.println("消息消费被中断");

        /**
         * 消费者消费消息
         * 1.队列名称
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.传递消息时的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
