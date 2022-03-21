package org.lucius.mq.work.fair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SendMq {

    private final  static String QUEUE_NAME="LUCIUS_Work_fair";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("lucius");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("lucius");
        connectionFactory.setPassword("lucius");

        Connection connection =null;
        Channel channel= null;
        try {
            connection =connectionFactory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            for (int i =0;i<20;i++){
                String message="哥也来了"+i;
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes("utf-8"));
                System.out.println("发送完成"+i);
            }

        }catch (Exception e){

        }finally {
            if(channel !=null && channel.isOpen()){
                channel.close();
            }
            if(connection !=null && connection.isOpen()){
                connection.close();
            }
        }
    }
}
