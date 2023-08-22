package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-21 23:11
 */
public class ConsumerTwo {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("queueTwo", false, false, false, null);

        // 同一个队列和交换机  可以绑定多个不同的RoutingKey
        channel.queueBind("queueTwo", EXCHANGE_NAME, "error");
        channel.queueBind("queueTwo", EXCHANGE_NAME, "warning");

        System.out.println("消费者Two等待接收消息（通知模式---direct类型直接交换机）......");
        channel.basicConsume("queueTwo", true,
                (consumerTag, message) -> System.out.println("消费者Two接收消息：" + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTag -> System.out.println("消费者Two取消消费者接口回调逻辑"));
    }
}
