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
 * 顺序消息消费 - 分区有序
 * 该消费者类用于处理顺序消息，即确保同一分区中的消息按照顺序被消费。
 * 仅处理 TagA 的消息，忽略其他 Tag 的消息。
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
        consumeMode = ConsumeMode.ORDERLY,
        // 只订阅 TagA 的消息
        selectorExpression = "TagA"
)
public class ConsumerInOrderSpecialTag implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
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

        // 只处理 TagA 的消息
        // 外部标记其实已经足够，出于防御性编程的目的
        if ("TagA".equals(tag)) {
            try {
                log.info("Receive message with TagA: {}  ThreadName: {}", body, Thread.currentThread().getName());
                // 处理 TagA 的业务逻辑
                handleTagA(body);
                log.info("Message processed successfully: {}", body);
            } catch (Exception e) {
                log.error("Failed to process message: {}  Error: {}", body, e.getMessage(), e);
                throw new UnsupportedOperationException("Message processing failed", e);
            }
        } else {
            // 记录收到非 TagA 的消息并忽略
            log.info("Ignore message with tag: {}  Body: {}", tag, body);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
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
}
