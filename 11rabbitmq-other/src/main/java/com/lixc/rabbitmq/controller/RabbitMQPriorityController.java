package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.RabbitMQPriorityConfig;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2023-08-27 0:00
 */
@Slf4j
@RestController
@RequestMapping("/priority")
public class RabbitMQPriorityController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{sendMessage}")
    public void sendMsg(@PathVariable String sendMessage) {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                rabbitTemplate.convertAndSend("", RabbitMQPriorityConfig.PRIORITY_QUEUE, sendMessage + i,
                        message -> {
                            message.getMessageProperties().setPriority(5);
                            message.getMessageProperties().setExpiration("10000");
                            return message;
                        });
            } else {
                rabbitTemplate.convertAndSend("", RabbitMQPriorityConfig.PRIORITY_QUEUE, sendMessage + i,
                        message -> {
                            message.getMessageProperties().setExpiration("10000");
                            return message;
                        });
            }
        }
    }
}
