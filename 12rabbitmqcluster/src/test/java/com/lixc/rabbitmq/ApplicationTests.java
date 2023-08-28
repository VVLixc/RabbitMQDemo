package com.lixc.rabbitmq;

import com.lixc.rabbitmq.config.MirrorQueueConfig;
import com.lixc.rabbitmq.producer.MirrorQueueProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootTest
class ApplicationTests {
    @Autowired
    MirrorQueueProducer mirrorQueueProducer;

    @Test
    void contextLoads() {
        mirrorQueueProducer.sendMsg("lixc");
    }

    @Test
    void testProducer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.222.133");
        connectionFactory.setUsername("lixc");
        connectionFactory.setPassword("001214");
        Channel channel = connectionFactory.newConnection().createChannel();
        String message = "vivi";
        //channel.queueDeclare(MirrorQueueConfig.MIRROR_QUEUE, true, false, false, null);
        channel.basicPublish(MirrorQueueConfig.MIRROR_EXCHANGE, MirrorQueueConfig.MIRROR_ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
        log.info("生产者发送消息：{}", message);

    }

    @Test
    void testConsumer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.222.133");
        connectionFactory.setUsername("lixc");
        connectionFactory.setPassword("001214");
        Channel channel = connectionFactory.newConnection().createChannel();

        channel.basicConsume(MirrorQueueConfig.MIRROR_QUEUE, true,
                (String consumerTag, Delivery message) -> {
                    log.info("消费者接收消息：{}", new String(message.getBody(), StandardCharsets.UTF_8));
                }, consumerTag -> {
                });
    }

}
