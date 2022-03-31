package org.lucius.mq.publish.subscrible.tx;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * AMQP事务控制
 */
public class SendMq {

    private final  static String QUEUE_NAME="tx";
    private final  static String EXCHANGE_NAME="PS_TEST";
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
            channel.txSelect();
//            创建交换机，只要绑定到交换机，就可以将消息路由到对应队列上
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
//            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message="爷来了";
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("utf-8"));
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("utf-8"));
            int i=2/0;
            System.out.println("发送完成");
            channel.txCommit();
        }catch (Exception e){

            channel.txRollback();
            System.out.println("已回滚");
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
