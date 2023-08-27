package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.PriorityConfig;
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
 * @since 2023-08-27 18:02
 */
@Slf4j
@RestController
@RequestMapping("/priority")
public class PriorityController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg")
    public void sendMsg() {
        for (int i = 1; i <= 10; i++) {
            String sendMessage = i + "";
            if (i == 7) {
                rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE,
                        PriorityConfig.PRIORITY_ROUTING_KEY,
                        sendMessage,
                        message -> {
                            // 设置生产者发送的消息优先级（不设定默认为0，数值越大优先级越高）
                            message.getMessageProperties().setPriority(5);
                            // 设置生产者发送消息的过期时长
                            message.getMessageProperties().setExpiration("5000");
                            return message;
                        });
            } else {
                rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE,
                        PriorityConfig.PRIORITY_ROUTING_KEY,
                        sendMessage,
                        message -> {
                            message.getMessageProperties().setExpiration("5000");
                            return message;
                        });
            }
        }
    }
}
