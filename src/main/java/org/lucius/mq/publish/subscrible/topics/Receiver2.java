package org.lucius.mq.publish.subscrible.topics;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

public class Receiver2 {
    private final  static String QUEUE_NAME="goods_update";
    private final  static String EXCHANGE_NAME="PS_TEST_TOPICS";

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
//            绑定队列到交换机  可绑定多个
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"goods.update.#");
//            每次接受消息的数量 小于等于1
            channel.basicQos(2);
            Channel finalChannel = channel;
            Consumer consumer = new DefaultConsumer(finalChannel){
                @SneakyThrows
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body,"utf-8");
                    System.out.println("商品修改消息："+message);
                    Thread.sleep(1000);
                   //  手动回执消息                         回执的消息                 一次回执一条消息
                    finalChannel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
            Boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME,autoAck,consumer);
        }catch (Exception e){

        }



    }
}
