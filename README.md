# SpringBoot 集成 RocketMQ

# MQ 基本概念

## MQ 简介

## MQ 的作用

## MQ 的优缺点

## MQ 的构成

# Docker 启动 RocketMQ

## 启动 NameSrv

## 启动 Broker

# RokcetMQ

## 生产者

## 消费者

## 消息事务

## 消息分类

根据 Topic，tag，key 等进行分类。

在 RocketMQ 中，确实可以根据 `topic` 和 `key` 对消息进行分类和过滤。下面是如何实现这种分类的几种方式：

### 1. 根据 Topic 分类

一个 `topic` 代表一个消息类别，你可以为不同类型的消息创建不同的 `topic`。生产者根据消息类型发送到不同的 `topic`，消费者订阅相应的 `topic` 来接收特定类型的消息。

**生产者代码示例：**
```java
DefaultMQProducer producer = new DefaultMQProducer("producer-group");
producer.setNamesrvAddr("localhost:9876");
producer.start();

Message message1 = new Message("topicA", "tagA", "keyA", "Hello topicA".getBytes());
producer.send(message1);

Message message2 = new Message("topicB", "tagB", "keyB", "Hello topicB".getBytes());
producer.send(message2);

producer.shutdown();
```

**消费者代码示例：**
```java
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group");
consumer.setNamesrvAddr("localhost:9876");
consumer.subscribe("topicA", "*");

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            System.out.println(new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});

consumer.start();
```

### 2. 根据 Tag 分类

`Tag` 是消息的二级分类，可以在一个 `topic` 下进一步对消息进行分类。这样消费者可以根据 `tag` 进行过滤，接收特定类别的消息。

**生产者代码示例：**
```java
Message message1 = new Message("topicA", "tagA", "keyA", "Hello tagA".getBytes());
producer.send(message1);

Message message2 = new Message("topicA", "tagB", "keyB", "Hello tagB".getBytes());
producer.send(message2);
```

**消费者代码示例：**
```java
consumer.subscribe("topicA", "tagA"); // 只订阅 tagA 的消息

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            System.out.println(new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

### 3. 根据 Key 分类

`Key` 用于标识消息，可以用作消息的唯一标识符或用于特定业务逻辑处理。虽然 `key` 不能直接用于消息过滤，但可以在消费者接收到消息后，通过 `key` 进行业务逻辑分类。

**生产者代码示例：**
```java
Message message1 = new Message("topicA", "tagA", "keyA", "Hello keyA".getBytes());
producer.send(message1);

Message message2 = new Message("topicA", "tagA", "keyB", "Hello keyB".getBytes());
producer.send(message2);
```

**消费者代码示例：**
```java
consumer.subscribe("topicA", "*"); // 订阅 topicA 下所有 tag 的消息

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            String key = msg.getKeys();
            if ("keyA".equals(key)) {
                System.out.println("Processing message with keyA: " + new String(msg.getBody()));
            } else if ("keyB".equals(key)) {
                System.out.println("Processing message with keyB: " + new String(msg.getBody()));
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

### 4. 使用 SQL92 过滤

RocketMQ 还支持使用 SQL92 表达式进行消息过滤，这允许在订阅消息时使用更复杂的过滤条件。

**生产者代码示例：**
```java
Message message1 = new Message("topicA", "tagA", "keyA", "Hello SQL filter".getBytes());
message1.putUserProperty("a", "1");
producer.send(message1);

Message message2 = new Message("topicA", "tagA", "keyB", "Hello SQL filter".getBytes());
message2.putUserProperty("a", "2");
producer.send(message2);
```

**消费者代码示例：**

```java
consumer.subscribe("topicA", MessageSelector.bySql("a = 1"));

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            System.out.println("Processing message with property a = 1: " + new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

以上是几种在 RocketMQ 中对消息进行分类和过滤的方法，可以根据具体的业务需求选择合适的方案。

# SpringBoot 集成 RocketMQ



