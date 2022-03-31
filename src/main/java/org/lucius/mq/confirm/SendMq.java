package org.lucius.mq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 同步confirm
 */
public class SendMq {

    private final  static String QUEUE_NAME="LUCIUS_TEST";
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
//            开启confirm确认
            channel.confirmSelect();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message="爷来了";
            int i = 1/0;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("utf-8"));
            if(channel.waitForConfirms()) {

                System.out.println("发送完成");
            }else{
                System.out.println("发送失败");
            }
            channel.waitForConfirmsOrDie();
            System.out.println("发送结束");
        }catch (Exception e){

            e.printStackTrace();
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
