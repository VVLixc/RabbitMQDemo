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
 * @since 2023-08-27 11:20
 */
@Configuration
public class PublishConfirmConfig {
    public static final String CONFIRM_QUEUE = "confirm_queue";
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";
    // 备份交换机
    public static final String ALTERNATE_EXCHANGE = "alternate_exchange";
    // 备份队列
    public static final String ALTERNATE_QUEUE = "alternate_queue";
    // 警告队列
    public static final String WARNING_QUEUE = "warning_queue";

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public DirectExchange confirmExchange() {
        //return new DirectExchange(CONFIRM_EXCHANGE);
        // 声明当前交换机无法路由消息时，会将消息转发到哪一个备份交换机。
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true).withArgument("alternate-exchange", ALTERNATE_EXCHANGE).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                                      @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    // 声明备份队列
    @Bean
    public Queue alternateQueue() {
        return QueueBuilder.durable(ALTERNATE_QUEUE).build();
    }

    // 声明警告队列
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    // 声明备份交换机
    @Bean
    public FanoutExchange alternateExchange() {
        return new FanoutExchange(ALTERNATE_EXCHANGE);
    }

    // 绑定 备份队列与备份交换机
    @Bean
    public Binding alternateQueueBindingAlternateExchange(@Qualifier("alternateQueue") Queue alternateQueue,
                                                          @Qualifier("alternateExchange") FanoutExchange alternateExchange) {
        return BindingBuilder.bind(alternateQueue).to(alternateExchange);
    }

    // 绑定 警告队列与备份交换机
    @Bean
    public Binding warningQueueBindingAlternateExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                          @Qualifier("alternateExchange") FanoutExchange alternateExchange) {
        return BindingBuilder.bind(warningQueue).to(alternateExchange);
    }
}
