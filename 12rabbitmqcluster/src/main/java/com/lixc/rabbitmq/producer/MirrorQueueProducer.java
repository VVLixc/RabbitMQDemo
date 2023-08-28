package com.lixc.rabbitmq.producer;

import com.lixc.rabbitmq.config.MirrorQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * com.lixc.rabbitmq.producer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-28 18:58
 */
@Slf4j
@Component
public class MirrorQueueProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String message){
        rabbitTemplate.convertAndSend(MirrorQueueConfig.MIRROR_EXCHANGE, MirrorQueueConfig.MIRROR_ROUTING_KEY, message);
        log.info("生产者发送消息：{}",message);
    }
}
