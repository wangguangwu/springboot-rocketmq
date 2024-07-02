package com.wangguangwu.springbootrocketmqconsumer.service;

import com.wangguangwu.springbootrocketmqconsumer.dto.OrderStepDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * 顺序消息消费 - 分区有序
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
public class ConsumerInOrder implements RocketMQListener<OrderStepDTO>, RocketMQPushConsumerLifecycleListener {

    /**
     * 处理接收到的消息。
     *
     * @param orderStepDTO 收到的消息对象
     */
    @Override
    public void onMessage(OrderStepDTO orderStepDTO) {
        log.info("Receive message: {}  ThreadName: {}", orderStepDTO, Thread.currentThread().getName());
        // 这里可以添加更多的业务逻辑来处理消息
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

        // 这里可以添加更多的消费者配置
    }
}

