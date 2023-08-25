package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQDelayMsgConfig;
import com.lixc.rabbitmq.config.RabbitMQMsgTTLConfig;
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

    @GetMapping("/sendDelayMsg/{sendMessage}/{delayTime}")
    public String sendDelayMsg(@PathVariable String sendMessage, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(RabbitMQDelayMsgConfig.DELAY_EXCHANGE,
                RabbitMQDelayMsgConfig.DELAY_ROUTING_KEY,
                sendMessage,
                message -> {
                    message.getMessageProperties().setDelay(delayTime);
                    return message;
                }
        );
        log.info("生产者---当前时间：{}，发送消息：{}，交换机中定义的延迟时间：{}ms", new Date().toString(), sendMessage, delayTime);
        return "RabbitMQ延迟队列---消息发送成功";
    }

    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable("message") String message) {
        log.info("生产者---当前时间：{}，发送消息：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend(RabbitMQMsgTTLConfig.NORMAL_EXCHANGE, RabbitMQMsgTTLConfig.NORMAL_ROUTING_KEY_A, message);
        rabbitTemplate.convertAndSend(RabbitMQMsgTTLConfig.NORMAL_EXCHANGE, RabbitMQMsgTTLConfig.NORMAL_ROUTING_KEY_B, message);
        return "消息发送成功";
    }

    @GetMapping("/sendTTLMsg/{sendMessage}/{ttl}")
    public String sendTTLMsg(@PathVariable String sendMessage, @PathVariable String ttl) {
        rabbitTemplate.convertAndSend(RabbitMQMsgTTLConfig.NORMAL_EXCHANGE, RabbitMQMsgTTLConfig.NORMAL_ROUTING_KEY_C, sendMessage,
                message -> {
                    message.getMessageProperties().setExpiration(ttl);
                    System.out.println();
                    return message;
                });
        log.info("生产者---当前时间：{}，设置过期时长：{}，发送消息：{}", new Date().toString(), ttl, sendMessage);
        return "消息发送成功";
    }
}
