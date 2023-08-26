package com.lixc.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
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
 * @since 2023-08-26 11:35
 */
@Configuration
public class RabbitMQDelayQueueConfig {
    // 延迟队列名
    public static final String DELAY_QUEUE = "delay_queue";
    // 延迟交换机名
    public static final String DELAY_EXCHANGE = "delay_exchange";
    // 延迟队列名、交换机之间的 RoutingKey
    public static final String DELAY_ROUTING_KEY = "delay_routing_key";

    // 声明基于RabbitMQ插件的延迟队列
    @Bean
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE);
    }

    // 声明基于RabbitMQ插件的延迟交换机
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE,
                "x-delayed-message", true, false,
                arguments);
    }

    // 绑定基于RabbitMQ插件的队列与交换机
    @Bean
    public Binding delayQueueBindingDelayExchange(@Qualifier("delayQueue") Queue delayQueue,
                                                  @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY).noargs();
    }
}
