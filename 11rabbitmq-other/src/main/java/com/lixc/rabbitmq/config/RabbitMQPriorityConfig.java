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
 * @since 2023-08-26 23:57
 */
@Configuration
public class RabbitMQPriorityConfig {
    public static final String PRIORITY_QUEUE = "priority_queue";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    @Bean
    public Queue priorityQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return QueueBuilder.durable(PRIORITY_QUEUE).withArguments(arguments).build();
    }

    @Bean
    public Queue dead_queue() {
        return new Queue(DEAD_QUEUE);
    }

    @Bean
    public DirectExchange dead_exchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean
    public Binding dead_exchangeBindingDead_queue(@Qualifier("dead_queue") Queue dead_queue,
                                                  @Qualifier("dead_exchange") DirectExchange dead_exchange) {
        return BindingBuilder.bind(dead_queue).to(dead_exchange).with(DEAD_ROUTING_KEY);
    }
}
