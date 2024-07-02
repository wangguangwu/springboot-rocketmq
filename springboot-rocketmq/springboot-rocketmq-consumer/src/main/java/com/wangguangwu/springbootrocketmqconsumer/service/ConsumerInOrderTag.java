package com.wangguangwu.springbootrocketmqconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * 顺序消息消费（根据Tag） - 分区有序
 * 该消费者类用于处理顺序消息，即确保同一分区中的消息按照顺序被消费。
 *
 * @author wangguangwu
 */
@Slf4j
@Component
@RocketMQMessageListener(
        // 指定消费的主题
        topic = "consumer-inorder-topic",
        // 指定消费组
        consumerGroup = "consumer-inorder-group",
        // 设置为顺序消费模式
        consumeMode = ConsumeMode.ORDERLY
)
public class ConsumerInOrderTag implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    /**
     * 处理接收到的消息。
     *
     * @param messageExt 收到的消息对象
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        // 获取消息的 tag
        String tag = messageExt.getTags();
        // 获取消息体
        String body = new String(messageExt.getBody());

        // 根据 tag 进行不同的处理
        switch (tag) {
            case "TagA":
                log.info("Receive message with TagA: {}  ThreadName: {}", body, Thread.currentThread().getName());
                // 处理 TagA 的业务逻辑
                handleTagA(body);
                break;
            case "TagB":
                log.info("Receive message with TagB: {}  ThreadName: {}", body, Thread.currentThread().getName());
                // 处理 TagB 的业务逻辑
                handleTagB(body);
                break;
            default:
                log.warn("Receive message with unknown tag: {}  ThreadName: {}", body, Thread.currentThread().getName());
                // 处理其他未识别的 tag 的业务逻辑
                handleUnknownTag(body);
                break;
        }
    }

    /**
     * 配置消费者启动时的参数。
     *
     * @param consumer 默认的消息推送消费者
     */
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // 当程序报错时，顺序消息不会等待重试，而是立即执行。
        // 设置最大重试次数为3次，如果不设置最大重试次数，会一直不断重试执行。
        consumer.setMaxReconsumeTimes(3);

        // 设置消费位点，默认从上一个偏移量开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 可以添加更多的消费者配置
    }

    // 处理 TagA 的业务逻辑
    private void handleTagA(String message) {
        // 业务逻辑代码
        log.info("Handling TagA message: {}", message);
        // 模拟处理失败
        if (message.contains("fail")) {
            throw new UnsupportedOperationException("Simulated processing failure for TagA");
        }
    }

    // 处理 TagB 的业务逻辑
    private void handleTagB(String message) {
        // 业务逻辑代码
        log.info("Handling TagB message: {}", message);
    }

    // 处理未识别 Tag 的业务逻辑
    private void handleUnknownTag(String message) {
        // 业务逻辑代码
        log.warn("Handling unknown tag message: {}", message);
    }
}
