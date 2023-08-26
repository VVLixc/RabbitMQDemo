package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQPriorityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.consumer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-27 0:03
 */
@Slf4j
@Component
public class RabbitMQPriorityConsumer {
    @RabbitListener(queues = RabbitMQPriorityConfig.DEAD_QUEUE)
    public void receiveMsg(Message message) {
        log.info("消费者接收消息：{}", new String(message.getBody(), StandardCharsets.UTF_8));
    }
}
