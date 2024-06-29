package com.wangguangwu.prodcuer;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息生产者
 *
 * @author wangguangwu
 */
public class BatchProducer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        // 配置 producer
        DefaultMQProducer producer = new DefaultMQProducer("produce-group");
        producer.setNamesrvAddr(Constants.NAME_SERVER);
        producer.start();

        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            // 设置消息主题、标签
            Message message = new Message();
            message.setTopic("batch-topic");
            message.setKeys("batch-key");
            message.setTags("batch-tag");

            // 设置消息内容
            message.setBody("Batch message: hello world".getBytes());
            messageList.add(message);
        }

        // 发送消息
        producer.send(messageList);

        producer.shutdown();
    }
}
