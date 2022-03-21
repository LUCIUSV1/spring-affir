package org.lucius.mq.publish.subscrible.direct;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

public class Receiver1 {
    private final  static String QUEUE_NAME="info";
    private final  static String EXCHANGE_NAME="PS_TEST_DIRECT";

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
//            将队列绑定到指定交换机
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"info");
            channel.basicQos(1);
            Channel finalChannel = channel;

            Consumer consumer = new DefaultConsumer(channel){
                @SneakyThrows
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body,"utf-8");
                    System.out.println("info消息："+message);
                    Thread.sleep(2000);
                    //  手动回执消息                         回执的消息                 一次回执一条消息
                    finalChannel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
//            设置回执为手动
            Boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME,autoAck,consumer);
        }catch (Exception e){

        }



    }
}
