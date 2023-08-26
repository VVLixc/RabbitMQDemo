package com.lixc.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * com.lixc.rabbitmq.config
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-26 21:42
 */
@Configuration
public class RabbitMQPusblishConfirmConfig {
    public static final String CONFIRM_QUEUE = "confirm_queue";
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";
    // 备份交换机
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    // 备份队列
    public static final String BACKUP_QUEUE = "backup_queue";
    // 警告队列
    public static final String WARNING_QUEUE = "warning_queue";

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public DirectExchange confirmExchange() {
        //return new DirectExchange(CONFIRM_EXCHANGE);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE).build();
    }

    @Bean
    public Binding confirmQueueBingdingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                                @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                     @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
