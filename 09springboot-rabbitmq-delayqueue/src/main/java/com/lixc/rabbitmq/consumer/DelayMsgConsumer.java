package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQDelayMsgConfig;
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
 * @since 2023-08-25 16:32
 */
@Slf4j
@Component
public class DelayMsgConsumer {
    @RabbitListener(queues = RabbitMQDelayMsgConfig.DELAY_QUEUE)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者---当前时间：{}，延迟队列消息：{}", new Date().toString(), msg);

    }
}
