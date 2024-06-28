package com.wangguangwu.demo;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 生产者代码示例
 *
 * @author wangguangwu
 */
public class Producer {

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = null;
        try {
            // 创建消息生产者，指定生产者所属的组名
            producer = new DefaultMQProducer("producer-group");
            // 指定 NameServer 地址
            producer.setNamesrvAddr(Constants.NAME_SERVER);
            // 启动生产者
            producer.start();

            // 创建消息对象、指定主题、标签和消息体
            Message message = new Message("topic", "tag", "key", "hello world".getBytes());

            // 发送消息
            SendResult sendResult = producer.send(message, 10000);
            System.out.println("Produce result:：" + sendResult);
        } finally {
            if (producer != null) {
                producer.shutdown();
            }
        }
    }
}
