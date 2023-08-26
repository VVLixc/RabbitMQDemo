package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQPusblishConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * com.lixc.rabbitmq.controller
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-26 21:47
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class RabbitMQPublishConfirmController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable String message) {
        String queue = RabbitMQPusblishConfirmConfig.CONFIRM_QUEUE;
        String exchange = RabbitMQPusblishConfirmConfig.CONFIRM_EXCHANGE;
        String routingKey = RabbitMQPusblishConfirmConfig.CONFIRM_ROUTING_KEY;
        rabbitTemplate.convertAndSend(exchange,
                routingKey, message,
                new CorrelationData("1"));
        log.info("生产者发送消息：{}，当前交换机：{}，当前RoutingKey：{}", message, exchange, routingKey);

        routingKey = routingKey + "001214";
        rabbitTemplate.convertAndSend(exchange,
                routingKey, message,
                new CorrelationData("2"));
        log.info("生产者发送消息：{}，当前交换机：{}，当前RoutingKey：{}", message, exchange, routingKey);
        return "消息发送完成";
    }
}
