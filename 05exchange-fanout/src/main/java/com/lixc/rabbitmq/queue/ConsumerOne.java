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
public class ConsumerOne {
    public static final String EXCHANGE_NAME = "custom_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明交换机（交换机名称、类型）
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个临时队列（消费者断开和该队列的连接，队列将被自动删除）。队列名称随机
        String queueName = channel.queueDeclare().getQueue();
        //绑定（交换机和队列之间的绑定）：队列名，交换机明，RoutingKEY（也称之为BindingKey）
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("消费者One等待接收消息（发布订阅模式---fanout类型交换机）......");
        channel.basicConsume(queueName, true,
                (String consumerTag, Delivery message) ->
                        System.out.println("消费者One接收的消息：" + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTag -> System.out.println("消费者One取消消费者接口回调逻辑"));
    }
}
