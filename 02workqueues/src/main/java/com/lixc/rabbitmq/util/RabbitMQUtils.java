package com.lixc.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * com.lixc.rabbitmq.util
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 19:09
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.222.132");
        connectionFactory.setUsername("lixc");
        connectionFactory.setPassword("001214");
        Channel channel = connectionFactory.newConnection().createChannel();
        return channel;
    }
}
