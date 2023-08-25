package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * com.lixc.rabbitmq.consumer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-25 11:02
 */
@Slf4j
@Component
public class MsgTTLConsumer {
    @RabbitListener(queues = RabbitMQConfig.NORMAL_QUEUE_A)
    public void consumeQueueA(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者---当前时间：{}，消费普通队列A消息：{}", new Date().toString(), msg);
    }

    //@RabbitListener(queues = RabbitMQConfig.NORMAL_QUEUE_B)
    public void consumeQueueB(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者---当前时间：{}，消费普通队列B消息：{}", new Date().toString(), msg);
    }

    @RabbitListener(queues = RabbitMQConfig.DEAD_QUEUE)
    public void consumeDeadLetterQueue(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者---当前时间：{}，消费延迟队列（死信队列来源之一>>>消息TTL过期）消息：{}", new Date().toString(), msg);
    }
}
