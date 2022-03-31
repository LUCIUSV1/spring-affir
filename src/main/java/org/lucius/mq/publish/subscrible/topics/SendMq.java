package org.lucius.mq.publish.subscrible.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 *
 * 主题模式
 */
public class SendMq {

    private final  static String QUEUE_NAME="LUCIUS_TEST";
    private final  static String EXCHANGE_NAME="PS_TEST_TOPICS";
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
//            创建交换机，只要绑定到交换机，就可以将消息路由到对应队列上
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
//            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message="info 爷来了";
            String message1="error 爷来了";
            channel.basicPublish(EXCHANGE_NAME,"goods.select.get",null,message.getBytes("utf-8"));
            channel.basicPublish(EXCHANGE_NAME,"goods.update.get",null,message1.getBytes("utf-8"));
            System.out.println("发送完成");
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
