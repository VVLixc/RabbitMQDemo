package com.lixc.rabbitmq.queue.ack;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.lixc.rabbitmq.util.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.queue.ack
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 22:44
 */
public class ConsumerAck01 {
    public static final String QUEUE_NAME = "Ack_Queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        boolean autoAck = false;
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtils.sleep(1);
            System.out.println("接收到消息" + new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消消费接口回调逻辑");
        System.out.println("消费者01---等待接收消息（手动应答功能测试）...");
        //channel.basicQos(1);
        channel.basicQos(2);
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
