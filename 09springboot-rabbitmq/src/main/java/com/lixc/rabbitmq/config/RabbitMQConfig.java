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
 * @since 2023-08-24 23:46
 */
@Slf4j
@Configuration
public class RabbitMQConfig {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String NORMAL_A_ROUTING_KEY = "normal_a_routing_key";
    public static final String NORMAL_B_ROUTING_KEY = "normal_b_routing_key";
    public static final String DEAD_ROUTING_KEY = "deadKey";

    // 声明普通队列对应的交换机：normal_exchange
    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    // 声明死信队列对应的交换机：dead_exchange
    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    // 声明普通队列：normal_queue_a 同时将普通队列信息无法消费转到死信队列的信息配置
    @Bean("normalQueueA")
    public Queue normalQueueA() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(arguments).build();
    }

    // 声明普通队列：normal_queue_b
    @Bean("normalQueueB")
    public Queue normalQueueB() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        arguments.put("x-message-ttl", 30000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(arguments).build();
    }

    // 声明死信队列：dead_queue
    @Bean("deadQueue")
    public Queue deadQueue() {
        // return QueueBuilder.durable(DEAD_QUEUE).build();
        return new Queue(DEAD_QUEUE);
    }

    //绑定
    @Bean
    public Binding normalQueueABindingNormalExchange(@Qualifier("normalQueueA") Queue normalQueueA,
                                                     @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueA).to(normalExchange).with(NORMAL_A_ROUTING_KEY);
    }

    @Bean
    public Binding normalQueueBBindingNormalExchange(@Qualifier("normalQueueB") Queue normalQueueB,
                                                     @Qualifier("normalExchange") DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueB).to(normalExchange).with(NORMAL_B_ROUTING_KEY);
    }

    @Bean
    public Binding deadQueueBindingDeadExchange(@Qualifier("deadQueue") Queue deadQueue,
                                                @Qualifier("deadExchange") DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    }
}
