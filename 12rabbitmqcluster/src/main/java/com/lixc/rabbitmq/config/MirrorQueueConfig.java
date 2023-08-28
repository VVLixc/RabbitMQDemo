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
 * @since 2023-08-28 18:45
 */
@Configuration
public class MirrorQueueConfig {
    public static final String MIRROR_QUEUE = "mirror_queue";
    public static final String MIRROR_EXCHANGE = "mirror_exchange";
    public static final String MIRROR_ROUTING_KEY = "mirror_routing_key";

    @Bean
    public Queue mirrorQueue() {
        return QueueBuilder.durable(MIRROR_QUEUE).build();
    }

    @Bean
    public DirectExchange mirrorExchange() {
        return new DirectExchange(MIRROR_EXCHANGE);
    }

    @Bean
    public Binding mirrorQueueBindingMirrorExchange(@Qualifier("mirrorQueue") Queue mirrorQueue,
                                                    @Qualifier("mirrorExchange") DirectExchange mirrorExchange) {
        return BindingBuilder.bind(mirrorQueue).to(mirrorExchange).with(MIRROR_ROUTING_KEY);
    }
}
