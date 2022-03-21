package org.lucius.mq.work.fair;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

public class Receiver1 {
    private final  static String QUEUE_NAME="LUCIUS_Work_fair";

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
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel){
                @SneakyThrows
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body,"utf-8");
                    System.out.println("消费者1："+message);
                    Thread.sleep(2000);

                }
            };
            channel.basicConsume(QUEUE_NAME,true,consumer);
        }catch (Exception e){

        }



    }
}
