package com.lixc.rabbitmq.controller;

import com.lixc.rabbitmq.config.PublishConfirmConfig;
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
 * @since 2023-08-27 11:30
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class PublishConfirmController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{sendMessage}")
    public void sendMsg(@PathVariable String sendMessage) {
        // CorrelationData 设置消息ID及相关信息，生产者手动传递该对象实例，以完成交换机确认消息后的回调
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE,
                PublishConfirmConfig.CONFIRM_ROUTING_KEY, sendMessage, correlationData1);
        log.info("生产者发送消息：{}，当前交换机：{}，RoutingKey：{}",
                sendMessage, PublishConfirmConfig.CONFIRM_EXCHANGE, PublishConfirmConfig.CONFIRM_ROUTING_KEY);

        // 这里特意将交换机写错，测试交换机宕机
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE+"###",
                PublishConfirmConfig.CONFIRM_ROUTING_KEY, sendMessage, correlationData2);
        log.info("生产者发送消息：{}，当前交换机：{}，RoutingKey：{}",
                sendMessage, PublishConfirmConfig.CONFIRM_EXCHANGE+"###", PublishConfirmConfig.CONFIRM_ROUTING_KEY);

        // 这里特意将RoutingKey写错，测试交换机推送消息失败---消息无法路由
        CorrelationData correlationData3 = new CorrelationData("3");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE,
                PublishConfirmConfig.CONFIRM_ROUTING_KEY+"@@@", sendMessage, correlationData3);
        log.info("生产者发送消息：{}，当前交换机：{}，RoutingKey：{}",
                sendMessage, PublishConfirmConfig.CONFIRM_EXCHANGE, PublishConfirmConfig.CONFIRM_ROUTING_KEY+"@@@");
    }
}
