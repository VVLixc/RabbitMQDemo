package com.lixc.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

/**
 * com.lixc.rabbitmq.util
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-22 0:29
 */
public class RabbitMQUitls {
    public static Channel getChannel() throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.222.132");
        connectionFactory.setUsername("lixc");
        connectionFactory.setPassword("001214");
        return connectionFactory.newConnection().createChannel();
    }
}
