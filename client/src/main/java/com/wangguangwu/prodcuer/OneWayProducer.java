package com.wangguangwu.prodcuer;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 单向消息生产者
 *
 * @author wangguangwu
 */
public class OneWayProducer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 配置 producer
        DefaultMQProducer producer = new DefaultMQProducer("produce-group");
        producer.setNamesrvAddr(Constants.NAME_SERVER);
        producer.start();

        // 设置消息主题、标签
        Message message = new Message();
        message.setTopic("oneway-topic");
        message.setKeys("oneway-key");
        message.setTags("oneway-tag");

        // 设置消息内容
        message.setBody("OneWay message: hello world".getBytes());

        // 发送单向消息
        producer.sendOneway(message);

        producer.shutdown();
    }
}
