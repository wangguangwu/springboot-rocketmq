package com.wangguangwu.prodcuer;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 同步消息生产者
 *
 * @author wangguangwu
 */
public class SyncProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 配置 producer
        DefaultMQProducer producer = new DefaultMQProducer("produce-group");
        producer.setNamesrvAddr(Constants.NAME_SERVER);
        producer.start();

        // 设置消息主题、标签
        Message message = new Message();
        message.setTopic("sync-topic");
        message.setKeys("sync-key");
        message.setTags("sync-tag");

        // 设置消息内容
        message.setBody("Sync message: Hello World".getBytes());

        // 同步发送消息，等待消息发送结果
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult);

        producer.shutdown();
    }
}
