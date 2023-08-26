package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQDelayQueueConfig;
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
 * @since 2023-08-26 11:48
 */
@Slf4j
@RestController
@RequestMapping("/delay")
public class RabbitMQDelayQueueController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDelayMsg/{sendMessage}/{ttl}")
    public String sendDelayMsg(@PathVariable String sendMessage, @PathVariable Integer ttl) {
        rabbitTemplate.convertAndSend(RabbitMQDelayQueueConfig.DELAY_EXCHANGE,
                RabbitMQDelayQueueConfig.DELAY_ROUTING_KEY, sendMessage,
                message -> {
                    message.getMessageProperties().setDelay(ttl);
                    return message;
                });
        log.info("生产者发送消息：当前时间>>>{}，发送延迟队列>>>{}，消息TTL>>>{}ms，消息内容>>>{}",
                new Date().toString(), RabbitMQDelayQueueConfig.DELAY_EXCHANGE, ttl, sendMessage);
        return "消息发送成功";
    }
}
