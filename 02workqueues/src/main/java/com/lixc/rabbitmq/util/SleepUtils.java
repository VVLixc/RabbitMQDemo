package com.lixc.rabbitmq.util;

/**
 * com.lixc.rabbitmq.util
 *
 * @author Lixc
 * @version 1.0
 * @since 2023-08-20 22:52
 */
public class SleepUtils {
    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds* 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
