package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUitls;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-22 0:31
 */
public class Producer {
    public static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUitls.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        hashMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        hashMap.put("quick.orange.fox", "被队列 Q1 接收到");
        hashMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        hashMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        hashMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        hashMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        hashMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");

        for (String routingKey : hashMap.keySet()) {
            String message = hashMap.get(routingKey);
            System.out.println("生产者发送消息："+routingKey);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
