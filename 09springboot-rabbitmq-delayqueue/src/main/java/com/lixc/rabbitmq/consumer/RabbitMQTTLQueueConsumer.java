package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQTTLQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * com.lixc.rabbitmq.consumer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-26 11:00
 */
@Slf4j
@Component
public class RabbitMQTTLQueueConsumer {
    @RabbitListener(queues = RabbitMQTTLQueueConfig.DEAD_QUEUE)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者接收消息：当前时间>>>{}，当前队列>>>{}，消息内容>>>{}", new Date().toString(), RabbitMQTTLQueueConfig.DEAD_QUEUE, msg);
    }
}
