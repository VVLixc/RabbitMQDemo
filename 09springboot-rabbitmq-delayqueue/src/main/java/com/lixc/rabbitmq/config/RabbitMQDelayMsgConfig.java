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
 * @since 2023-08-25 16:15
 */
@Configuration
public class RabbitMQDelayMsgConfig {
    // 声明RabbitMQ插件对应的延迟队列名
    public static final String DELAY_QUEUE = "delay_queue";
    // 声明RabbitMQ插件对应的交换机名
    public static final String DELAY_EXCHANGE = "delay_exchange";
    // 声明RabbitMQ插件对应的RoutingKey
    public static final String DELAY_ROUTING_KEY = "delay_routing_key";

    // 声明RabbitMQ插件对应的延迟队列
    @Bean
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE);
    }

    // 声明RabbitMQ插件对应的交换机
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE, "x-delayed-message", true, false, arguments);
    }

    // 绑定
    @Bean
    public Binding delayQueueBindingDelayExchange(@Qualifier("delayQueue") Queue delayQueue,
                                                  @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY).noargs();
    }
}
