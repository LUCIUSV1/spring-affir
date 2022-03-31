package org.lucius.mq.confirmAsync;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/***
 * 异步confirm
 */
public class SendMq {

    private final  static String QUEUE_NAME="confirm_async";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("lucius");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("lucius");
        connectionFactory.setPassword("lucius");

        Connection connection =null;
        Channel channel= null;
        try {
            final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<>());
            connection =connectionFactory.newConnection();

            channel = connection.createChannel();
//            开启confirm确认
            channel.confirmSelect();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);


            channel.addConfirmListener(new ConfirmListener() {
//                成功
                @Override
                public void handleAck(long l, boolean b) throws IOException {
                    if(b){
                        // 已确认多条
                        System.out.println("已确认多条:"+l);
                        confirmSet.headSet(l);
                    }else{
                        //已确认单条
                        System.out.println("已确认单条:"+l);
                        confirmSet.remove(l);
                    }
                }
//失败
                @Override
                public void handleNack(long l, boolean b) throws IOException {
                    if(b){
                        // 已确认多条
                        System.out.println("已确认多条:"+l);
                        confirmSet.headSet(l).clear();
                    }else{
                        //已确认单条
                        System.out.println("已确认单条:"+l);
                        confirmSet.remove(l);
                    }
                }
            });
            while (true) {
                String message = "爷来了";
                Long sno = channel.getNextPublishSeqNo();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
                if (channel.waitForConfirms()) {

                    System.out.println("发送完成");
                } else {
                    System.out.println("发送失败");
                }
                channel.waitForConfirmsOrDie();
                System.out.println("发送结束");
                confirmSet.add(sno);
            }
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
