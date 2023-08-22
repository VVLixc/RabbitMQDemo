package com.lixc.rabbit;

import com.lixc.rabbit.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * com.lixc.rabbit
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-22 10:38
 */
public class PublishConfirmReview {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1.单个确认发布
        publishConfirmSingle();//1000条消息，单个发布确认耗时：1027ms；发布确认成功1000条
        // 2.批量确认发布
        publishConfirmBatch();//1000条消息，批量发布确认耗时：97ms；发布确认成功1000条
        // 3.异步确认发布
        publishConfirmAsync();//1000条消息，异步发布确认耗时：23ms
    }

    public static void publishConfirmSingle() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMQUtils.getChannel();
        // 在生产者信道开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);

        long start = System.currentTimeMillis();
        int publishConfirmSuccess = 0;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "单个发布确认-消息" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            // 服务端返回false或超时时间内未返回，生产者会消息重发。
            boolean flag = channel.waitForConfirms();
            if (flag) {
                ++publishConfirmSuccess;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，单个发布确认耗时：" + (end - start) + "ms；发布确认成功" + publishConfirmSuccess + "条");
    }

    public static void publishConfirmBatch() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMQUtils.getChannel();
        // 在生产者信道开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);

        long start = System.currentTimeMillis();
        int publishConfirmSuccess = 0;
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = "批量发布确认-消息" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            if (i % 100 == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    publishConfirmSuccess += 100;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，批量发布确认耗时：" + (end - start) + "ms；发布确认成功" + publishConfirmSuccess + "条");
    }

    public static void publishConfirmAsync() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMQUtils.getChannel();
        // 开启发布确认模式
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);

        // 用于存储待确认的消息的标识与消息的映射关系
        ConcurrentNavigableMap<Long, String> unConfirmMessages = new ConcurrentSkipListMap<>();

        // 注册发布确认回调函数：参数一消息序列号；
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 清除小于等于当前序列号的消息
                unConfirmMessages.headMap(deliveryTag, true).clear();
            } else {
                // 只清除当前序列号的消息
                unConfirmMessages.remove(deliveryTag);
            }
            System.out.println("异步确认发布监听成功：" + deliveryTag);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 处理发送失败的消息
            System.out.println(unConfirmMessages.get(deliveryTag) + "消息异步确认发布失败");
        };

        // 添加异步确认的监听器（监听消息发布的成功和失败）：参数一确认收到消息的回调；参数二未收到消息的回调
        channel.addConfirmListener(ackCallback, nackCallback);

        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "异步确认发布-消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            // channel.getNextPublishSeqNo()获取下一个消息的序列号：通过序列号与消息进行关联；此刻全是未确认的消息
            unConfirmMessages.put(channel.getNextPublishSeqNo(), message);
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，异步发布确认耗时：" + (end - start) + "ms");
    }
}
