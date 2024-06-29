package com.wangguangwu.prodcuer;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 异步消息生产者
 *
 * @author wangguangwu
 */
public class ASyncProducer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 配置 producer
        DefaultMQProducer producer = new DefaultMQProducer("produce-group");
        producer.setNamesrvAddr(Constants.NAME_SERVER);
        producer.start();

        // 设置消息主题、标签
        Message message = new Message();
        message.setTopic("async-topic");
        message.setKeys("async-key");
        message.setTags("async-tag");

        // 设置消息内容
        message.setBody("ASync message: Hello World".getBytes());

        // 发送消息，并设置正常异常情况
        producer.send(message, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }

            @Override
            public void onException(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
