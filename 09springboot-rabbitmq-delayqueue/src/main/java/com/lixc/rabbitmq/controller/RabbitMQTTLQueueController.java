package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQTTLQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * com.lixc.rabbitmq.controller
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-26 10:55
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class RabbitMQTTLQueueController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendTTLMsg/{sendMessage}/{ttl}")
    public String sendTTLMsg(@PathVariable String sendMessage, @PathVariable String ttl) {
        rabbitTemplate.convertAndSend(RabbitMQTTLQueueConfig.NORMAL_EXCHANGE,
                RabbitMQTTLQueueConfig.NORMAL_ROUTING_KEY, sendMessage,
                message -> {
                    message.getMessageProperties().setExpiration(ttl);
                    return message;
                });
        log.info("生产者发送消息：当前时间>>>{}，，发送队列>>>{}，消息TTL>>>{}ms，消息内容>>>{}",
                new Date().toString(), RabbitMQTTLQueueConfig.NORMAL_EXCHANGE, ttl, sendMessage);
        return "消息发送成功";
    }
}
