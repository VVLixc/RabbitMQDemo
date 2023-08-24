package com.lixc.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-08-24 23:58
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class TTLRabbitMQController {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String NORMAL_A_ROUTING_KEY = "normal_a_routing_key";
    public static final String NORMAL_B_ROUTING_KEY = "normal_b_routing_key";
    public static final String DEAD_ROUTING_KEY = "deadKey";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public String sendMsg(@PathVariable String message) {
        log.info("生产者>>>>>当前时间：{}，发送消息：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, NORMAL_A_ROUTING_KEY, message);
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, NORMAL_B_ROUTING_KEY, message);
        return "消息发送成功";
    }
}
