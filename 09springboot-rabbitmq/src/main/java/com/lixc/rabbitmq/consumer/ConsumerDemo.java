package com.lixc.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * com.lixc.rabbitmq.consumer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-25 0:12
 */
@Slf4j
@Component
public class ConsumerDemo {
    @RabbitListener(queues = "dead_queue")
    public void receiveMsg(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("消费者>>>>>当前时间：{}，收到死信队列信息{}", new Date().toString(), msg);
    }
    @RabbitListener(queues = "normal_queue_a")
    public void receiveNAMsg(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("消费者>>>>>当前时间：{}，收到普普通通A队列信息{}", new Date().toString(), msg);
    }
}
