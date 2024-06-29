package com.wangguangwu.transaction;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * 事务消息
 * <p>
 * 消费者
 *
 * @author wangguangwu
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {
        // 创建消费者实例，并指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction-consumer-group");

        // 设置NameServer地址
        consumer.setNamesrvAddr(Constants.NAME_SERVER);

        // 订阅主题，并指定订阅所有的标签（即 "*" 代表所有 tags）
        consumer.subscribe("transaction-topic", "*");

        // 注册消息监听器，当消息到达时触发该监听器进行消息处理
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            // 遍历处理每条消息
            msgs.forEach(messageExt -> {
                // 打印消费消息的线程名、队列ID和消息内容
                System.out.println("consumeThread=" + Thread.currentThread().getName() +
                        ", queueId=" + messageExt.getQueueId() + ", content: " + new String(messageExt.getBody()));
            });
            // 返回消费成功的状态
            return ConsumeOrderlyStatus.SUCCESS;
        });

        // 启动消费者实例
        consumer.start();
        // 打印消费者启动成功信息
        System.out.println("Consumer started.");
    }
}