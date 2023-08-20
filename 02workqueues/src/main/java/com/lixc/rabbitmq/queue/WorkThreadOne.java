package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * com.lixc.rabbitmq.queue
 * 工作线程---消费者
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 19:11
 */
public class WorkThreadOne {
    public static final String QUEUE_NAME = "Work Queues";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("接收到消息"+new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        System.out.println("WorkThread02等待接收消息...");
        /**
         * 消费者消费消息
         * 1.队列名称
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.传递消息时的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
