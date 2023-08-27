package com.lixc.rabbitmq.config;

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
 * @since 2023-08-27 17:57
 */
@Configuration
public class PriorityConfig {
    public static final String PRIORITY_QUEUE = "priority_queue";
    public static final String PRIORITY_EXCHANGE = "priority_exchange";
    public static final String PRIORITY_ROUTING_KEY = "priority_routing_key";
    // 声明死信队列：为了测试优先级队列功能；
    // 优先级队列是指多条消息被消费者接收消费的优先级，所以这里对生产的消息进行阻塞，然后消息们过期一股脑进入死信队列
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    @Bean
    public Queue priorityQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-queue-mode", "lazy");
        // 声明队列优先级最大范围：此处表示优先级区间0-10（0-255范围，但不推荐过大，影响CPU和内存）
        arguments.put("x-max-priority", 10);
        // 声明当前队列对应的死信队列（死信队列来源：消息TTL过期、队列过大、消息被拒绝）
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return QueueBuilder.durable(PRIORITY_QUEUE).withArguments(arguments).build();
    }

    @Bean
    public DirectExchange priorityExchange() {
        return new DirectExchange(PRIORITY_EXCHANGE);
    }

    @Bean
    public Binding priorityQueueBindingPriorityExchange(@Qualifier("priorityQueue") Queue priorityQueue,
                                                        @Qualifier("priorityExchange") DirectExchange priorityExchange) {
        return BindingBuilder.bind(priorityQueue).to(priorityExchange).with(PRIORITY_ROUTING_KEY);
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean
    public Binding deadQueueBindingDeadExchange(@Qualifier("deadQueue") Queue deadQueue,
                                                @Qualifier("deadExchange") DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    }
}
