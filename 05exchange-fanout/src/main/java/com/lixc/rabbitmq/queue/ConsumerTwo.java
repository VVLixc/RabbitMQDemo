package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-21 22:31
 */
public class ConsumerTwo {
    public static final String EXCHANGE_NAME = "custom_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("消费者Two等待接收消息......");
        channel.basicConsume(queueName, true,
                (String consumerTag, Delivery message) ->
                        System.out.println("消费者Two接收的消息：" + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTag -> System.out.println("消费者Two取消消费者接口回调逻辑"));

    }
}
