package com.lixc.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.config
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-27 11:38
 */
@Slf4j
@Component
public class CustomCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 想要实现交换机确认消息后自动回调该对象方法，必须将该回调类注入RabbitTemplate中。
        rabbitTemplate.setConfirmCallback(this);
        // 将此实现了RabbitMQ回退接口的对象注入RabbitTemplate中，实现回退消息。
        rabbitTemplate.setReturnsCallback(this);
    }

    // RabbitTemplate.ConfirmCallback接口的方法，交换机确认消息后回调的方法
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // ack：true表示交换机接收消息成功；反之失败
        if (ack) {
            log.info("交换机接收消息成功，消息Id：{}", correlationData.getId());
        } else {
            log.info("交换机接收消息失败，消息Id：{}，失败原因：{}", correlationData.getId(), cause);
        }
    }

    // RabbitTemplate.ReturnsCallback接口的方法：消息无法路由时回调的方法（回退消息）
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息无法路由>>>回退消息：交换机：{}，RoutingKey：{}，消息：{}，无法路由原因：{}",
                returned.getExchange(), returned.getRoutingKey(),
                new String(returned.getMessage().getBody(), StandardCharsets.UTF_8),
                returned.getReplyText());
    }
}
