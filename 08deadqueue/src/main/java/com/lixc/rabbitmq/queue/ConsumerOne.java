package com.lixc.rabbitmq.queue;

import com.lixc.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * com.lixc.rabbitmq.queue
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-23 0:14
 */
public class ConsumerOne {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 正常队列绑定死信队列信息
        Map<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "deadKey");
        // 死信来源：设置正常队列长度的限制
        //arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normalKey");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "deadKey");

//        channel.basicConsume(NORMAL_QUEUE, true,
//                (String consumerTag, Delivery message) -> System.out.println("C1接收消息：" + new String(message.getBody(), StandardCharsets.UTF_8)),
//                consumerTag -> {
//                });

        //死信来源：消息拒绝 autoAck参数设置false表示手动应答；basicReject、basicAck方法配合使用
        channel.basicConsume(NORMAL_QUEUE, false,
                (String consumerTag, Delivery message) -> {
                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    if ("消息7".equals(msg)) {
                        System.out.println("C1拒绝消息：" + msg);
                        channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                    } else {
                        System.out.println("C1接收消息：" + msg);
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    }
                },
                consumerTag -> {
                });
    }
}
