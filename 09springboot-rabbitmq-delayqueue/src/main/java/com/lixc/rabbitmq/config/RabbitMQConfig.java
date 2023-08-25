package com.lixc.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * com.lixc.rabbitmq.config
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-25 10:38
 */
@Slf4j
@Configuration
public class RabbitMQConfig {
    // 声明普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 声明死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 声明普通队列A
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    // 声明普通队列B
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    // 声明死信队列
    public static final String DEAD_QUEUE = "dead_queue";
    // 声明普通队列A & 普通交换机 之间的路由key/RoutingKey/BindingKey
    public static final String NORMAL_ROUTING_KEY_A = "normal_routing_key_a";
    // 声明普通队列B & 普通交换机 之间的路由key/RoutingKey/BindingKey
    public static final String NORMAL_ROUTING_KEY_B = "normal_routing_key_b";
    // 声明死信队列 & 死信交换机 之间的路由key/RoutingKey/BindingKey
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    // 声明普通交换机
    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    // 声明死信交换机
    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    // 声明普通队列A
    @Bean("normalQueueA")
    public Queue normalQueueA() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(arguments).build();
    }

    // 声明普通队列B
    @Bean("normalQueueB")
    public Queue normalQueueB() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        arguments.put("x-message-ttl", 20000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(arguments).build();
    }

    // 声明死信队列
    @Bean("deadQueue")
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    // 绑定：普通队列A&普通交换机；普通队列B&普通交换机；死信队列&死信交换机
    @Bean
    public Binding normalQueueAAndnormalExchange(@Qualifier("normalQueueA") Queue normalQueueA, @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueA).to(normalExchange).with(NORMAL_ROUTING_KEY_A);
    }

    @Bean
    public Binding normalQueueBAndnormalExchange(@Qualifier("normalQueueB") Queue normalQueueB, @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueB).to(normalExchange).with(NORMAL_ROUTING_KEY_B);
    }

    @Bean
    public Binding deadQueueAnddeadExchange(@Qualifier("deadQueue") Queue deadQueue, @Qualifier("deadExchange") DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    }
}
