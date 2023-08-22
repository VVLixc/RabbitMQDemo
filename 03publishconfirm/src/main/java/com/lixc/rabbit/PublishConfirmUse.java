package com.lixc.rabbit;

import com.lixc.rabbit.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * com.lixc.rabbit
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-21 17:21
 */
public class PublishConfirmUse {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1.单个确认发布
        //publishConfirmSingle();//1000条消息，耗时：593ms单个确认发布成功1000条
        // 2.批量确认发布
        //publishConfirmBatch();//1000条消息，耗时：105ms批量确认发布成功1000条
        // 3.异步确认发布
        publishConfirmAsync();//1000条消息，耗时：58ms

    }

    public static void publishConfirmSingle() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);
        long start = System.currentTimeMillis();

        int publishSuccess = 0;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
                ++publishSuccess;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms" + "单个确认发布成功" + publishSuccess + "条");
    }

    public static void publishConfirmBatch() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);
        long start = System.currentTimeMillis();

        int publishSuccess = 0;
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            if (i % 100 == 0) {
                //服务端返回 false 或超时时间内未返回，生产者可以消息重发
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    publishSuccess = publishSuccess + 100;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms" + "批量确认发布成功" + publishSuccess + "条");
    }

    public static void publishConfirmAsync() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);
        long start = System.currentTimeMillis();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirms = outstandingConfirms.headMap(deliveryTag, true);
                confirms.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("异步确认发布监听成功：" + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("异步确认发布监听失败：" + deliveryTag);
        };
        // 信道添加发布确认的监听器：监听消息发布的成功和失败
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            /**
             * channel.getNextPublishSeqNo()获取下一个消息的序列号
             * 通过序列号与消息体进行一个关联
             * 全部都是未确认的消息体
             */
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        }
        long end = System.currentTimeMillis();
        System.out.println(MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms");
    }

    public static void publishMessageAsync() throws Exception {
        try (Channel channel = RabbitMQUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                    ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息
             * false 确认当前序列号消息
             */
            ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
                if (multiple) {
                    //返回的是小于等于当前序列号的未确认消息 是一个 map
                    ConcurrentNavigableMap<Long, String> confirmed =
                            outstandingConfirms.headMap(sequenceNumber, true);
                    //清除该部分未确认消息
                    confirmed.clear();
                }else{
                    //只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
                String message = outstandingConfirms.get(sequenceNumber);
                System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, null);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = "消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, null, message.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息,耗时" + (end - begin) +
                    "ms");
        }
    }
}
