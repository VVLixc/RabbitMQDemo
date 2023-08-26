package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQDelayQueueConfig;
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
 * @since 2023-08-26 11:53
 */
@Slf4j
@Component
public class RabbitMQDelayQueueConsumer {
    @RabbitListener(queues = RabbitMQDelayQueueConfig.DELAY_QUEUE)
    public void receiveDelayMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者接收消息：当前时间>>>{}，当前队列>>>{}，消息内容>>>{}", new Date().toString(), RabbitMQDelayQueueConfig.DELAY_QUEUE, msg);

    }
}
