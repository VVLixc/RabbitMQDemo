package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 19:26
 */
public class Producer {
    public static final String QUEUE_NAME = "Work Queues";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("生产者消息发送："+message);
        }
    }
}
