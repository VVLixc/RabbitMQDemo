package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUitls;
import com.rabbitmq.client.BuiltinExchangeType;
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
 * @since 2023-08-22 0:31
 */
public class Consumer01 {
    public static final String EXCHANGE_NAME = "topic_exchange";
    public static final String QUEUE_NAME="queue01";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUitls.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");
        System.out.println("消费者01等待接收消息......");
        channel.basicConsume(QUEUE_NAME, true,
                (String consumerTag, Delivery message) ->
                        System.out.println("绑定关系：" + message.getEnvelope().getRoutingKey() +
                                " 获取的消息：" + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTag -> System.out.println("消费者取消消费者接口回调逻辑"));
    }
}
