package com.wangguangwu.transaction;

import com.wangguangwu.common.Constants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事务消息
 * <p>
 * 生产者
 *
 * @author wangguangwu
 */
public class Producer {

    public static void main(String[] args) throws MQClientException {
        // 创建事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("transaction-producer-group");
        // 注册到 NameServer
        producer.setNamesrvAddr(Constants.NAME_SERVER);

        // 设置事务监听器
        producer.setTransactionListener(new TransactionListener() {

            /**
             * 执行本地事务；在发送事务性消息时被调用。
             * @param msg Half(prepare) message - 半消息（预备消息）
             * @param arg Custom business parameter - 自定义业务参数
             * @return LocalTransactionState - 本地事务状态
             *         LocalTransactionState.COMMIT_MESSAGE 表示提交事务
             *         LocalTransactionState.ROLLBACK_MESSAGE 表示回滚事务
             *         LocalTransactionState.UNKNOW 表示事务状态未知
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                if (Constants.SUCCESS.equals(arg.toString())) {
                    // 提交事务
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (Constants.FAIL.equals(arg.toString())) {
                    // 回滚事务
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    return LocalTransactionState.UNKNOW;
                }
            }

            /**
             * 检查本地事务的状态；在事务消息的状态不明确时被调用。
             * @param msg Check message - 检查消息
             * @return LocalTransactionState - 本地事务状态
             *         LocalTransactionState.COMMIT_MESSAGE 表示提交事务
             *         LocalTransactionState.ROLLBACK_MESSAGE 表示回滚事务
             *         LocalTransactionState.UNKNOW 表示事务状态未知
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // 默认提交事务
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        // 启动事务生产者
        producer.start();

        // 创建消息
        Message message = new Message("transaction-topic", "transaction-tag", "transaction-key",
                "Hello, transaction.".getBytes());

        // 发送消息
        TransactionSendResult sendSuccessResult = producer.sendMessageInTransaction(message, Boolean.TRUE);
        System.out.println("执行成功类型，事务执行结果：" + sendSuccessResult);

        TransactionSendResult sendFalseResult = producer.sendMessageInTransaction(message, Boolean.FALSE);
        System.out.println("执行失败类型，事务执行结果：" + sendFalseResult);

    }
}
