package com.lixc.rabbitmq.consumer;

import com.lixc.rabbitmq.config.PriorityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * com.lixc.rabbitmq.consumer
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-27 18:08
 */
@Slf4j
@Component
public class PriorityConsumer {
    // 这里注释掉原配队列，测试堆积的消息们过期后消费的优先级
    //@RabbitListener(queues = PriorityConfig.PRIORITY_QUEUE)
    public void receivePriorityMsg(Message message) {
        log.info("消费者接收消息：{}，当前队列：{}，绑定的交换机是：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), PriorityConfig.PRIORITY_QUEUE, PriorityConfig.PRIORITY_EXCHANGE);
    }

    @RabbitListener(queues = PriorityConfig.DEAD_QUEUE)
    public void receiveDeadMsg(Message message) {
        log.info("消费者接收消息：{}，当前队列：{}，绑定的交换机是：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), PriorityConfig.DEAD_QUEUE, PriorityConfig.DEAD_EXCHANGE);
    }
}
