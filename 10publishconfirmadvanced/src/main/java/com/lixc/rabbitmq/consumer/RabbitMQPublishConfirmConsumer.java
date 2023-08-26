package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQPusblishConfirmConfig;
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
 * @since 2023-08-26 21:51
 */
@Slf4j
@Component
public class RabbitMQPublishConfirmConsumer {
    @RabbitListener(queues = RabbitMQPusblishConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirmQueueMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者接收消息：{}，当前队列：{}", msg, RabbitMQPusblishConfirmConfig.CONFIRM_QUEUE);
    }

    @RabbitListener(queues = RabbitMQPusblishConfirmConfig.BACKUP_QUEUE)
    public void receiveBackupQueueMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者接收消息：{}，当前队列：{}", msg, RabbitMQPusblishConfirmConfig.BACKUP_QUEUE);
    }

    @RabbitListener(queues = RabbitMQPusblishConfirmConfig.WARNING_QUEUE)
    public void receiveWarningQueueMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者接收消息：{}，当前队列：{}", msg, RabbitMQPusblishConfirmConfig.WARNING_QUEUE);
    }
}
