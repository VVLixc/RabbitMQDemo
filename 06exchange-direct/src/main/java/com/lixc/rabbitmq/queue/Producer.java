package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-21 23:10
 */
public class Producer {
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("info", "普通info消息");
        hashMap.put("warning", "警告warning消息");
        hashMap.put("error", "错误error消息");
        for (String routingKey : hashMap.keySet()) {
            String message = hashMap.get(routingKey);
            System.out.println("生产者声明direct类型直接交换机 " + EXCHANGE_NAME + " 发送消息：" + message);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
