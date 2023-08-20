package com.lixc.rabbitmq.queue.ack;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * com.lixc.rabbitmq.queue.ack
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 22:40
 */
public class ProducerAck {
    public static final String QUEUE_NAME="Ack_Queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //创建队列
        boolean durable=true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message=scanner.next();
            System.out.println("生产者消息："+message);
            AMQP.BasicProperties props= MessageProperties.PERSISTENT_TEXT_PLAIN;
            channel.basicPublish("", QUEUE_NAME, props, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
