package org.lucius.mq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.sql.SQLType;

public class ReceiverMq {
    private final  static String QUEUE_NAME="LUCIUS_TEST";

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("lucius");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("lucius");
        connectionFactory.setPassword("lucius");

        Connection connection = null;
        Channel channel = null;
        try {
            connection  = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                   String message = new String(body,"utf-8");
                    System.out.println(message);
                }
            };
            channel.basicConsume(QUEUE_NAME,true,consumer);
        }catch (Exception e){

        }



    }
}
