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
 * @since 2023-08-26 21:56
 */
@Slf4j
@Component
public class CustomCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    // 交换机确认后回调的方法（注意和队列有无异常无关）
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("交换机正常接收消息>>>消息ID：{}", correlationData.getId());
        } else {
            log.info("交换机接收消息失败>>>消息ID：{}，失败原因：{}", correlationData.getId(), cause);
        }
    }


    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息：{} 被退回，退回原因：{}，交换机：{}，RoutingKey：{}",
                new String(returned.getMessage().getBody(), StandardCharsets.UTF_8),
                returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
    }
}
