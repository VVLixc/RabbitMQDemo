package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.PublishConfirmConfig;
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
 * @since 2023-08-27 11:34
 */
@Slf4j
@Component
public class PublishConfirmConsumer {
    @RabbitListener(queues = PublishConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirmMsg(Message message) {
        log.info("消费者接收消息：{}，当前队列：{}，绑定的交换机是：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), PublishConfirmConfig.CONFIRM_QUEUE, PublishConfirmConfig.CONFIRM_EXCHANGE);
    }

    @RabbitListener(queues = PublishConfirmConfig.ALTERNATE_QUEUE)
    public void receiveAlternateMsg(Message message) {
        log.info("消费者接收消息：{}，当前队列：{}，绑定的交换机是：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), PublishConfirmConfig.ALTERNATE_QUEUE, PublishConfirmConfig.ALTERNATE_EXCHANGE);
    }

    @RabbitListener(queues = PublishConfirmConfig.WARNING_QUEUE)
    public void receiveWarningMsg(Message message) {
        log.info("消费者接收消息：{}，当前队列：{}，绑定的交换机是：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), PublishConfirmConfig.WARNING_QUEUE, PublishConfirmConfig.ALTERNATE_EXCHANGE);
    }
}
