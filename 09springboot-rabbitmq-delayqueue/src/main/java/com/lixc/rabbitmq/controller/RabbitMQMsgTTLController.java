package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQConfig;
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
 * @since 2023-08-25 10:56
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class RabbitMQMsgTTLController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable("message") String message) {
        log.info("生产者---当前时间：{}，发送消息：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.NORMAL_EXCHANGE, RabbitMQConfig.NORMAL_ROUTING_KEY_A, message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.NORMAL_EXCHANGE, RabbitMQConfig.NORMAL_ROUTING_KEY_B, message);
        return "消息发送成功";
    }
}
