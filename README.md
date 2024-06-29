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

## 消息类型

在 RocketMQ 中，不同的消息发送方式满足不同的应用场景和业务需求。以下是同步消息、异步消息、单向消息和批量消息的定义及其适用场景：

### 同步消息（Synchronous Message）
- **定义**：同步消息是指生产者发送消息后等待服务器返回发送结果的消息发送方式。
- **适用场景**：适用于对消息可靠性要求较高的业务场景，如重要通知、订单生成等。因为生产者会在发送消息后等待服务器的确认，确保消息已经成功发送到服务器。
- **示例代码**：
  ```java
  DefaultMQProducer producer = new DefaultMQProducer("producer-group");
  producer.setNamesrvAddr("localhost:9876");
  producer.start();

  Message message = new Message("topic", "tag", "Hello, RocketMQ".getBytes());
  SendResult sendResult = producer.send(message);
  System.out.println("Send Result: " + sendResult);

  producer.shutdown();
  ```

### 异步消息（Asynchronous Message）
- **定义**：异步消息是指生产者发送消息后，不等待服务器返回结果，而是通过回调函数处理服务器返回的结果。
- **适用场景**：适用于对响应时间有要求的业务场景，如用户请求的快速响应。生产者在发送消息后可以立即返回，服务器处理结果通过回调函数异步通知。
- **示例代码**：
  ```java
  DefaultMQProducer producer = new DefaultMQProducer("producer-group");
  producer.setNamesrvAddr("localhost:9876");
  producer.start();

  Message message = new Message("topic", "tag", "Hello, RocketMQ".getBytes());
  producer.send(message, new SendCallback() {
      @Override
      public void onSuccess(SendResult sendResult) {
          System.out.println("Send Success: " + sendResult);
      }

      @Override
      public void onException(Throwable e) {
          e.printStackTrace();
          System.out.println("Send Failed");
      }
  });

  producer.shutdown();
  ```

### 单向消息（One-way Message）
- **定义**：单向消息是指生产者只负责发送消息，不等待服务器返回结果，也没有回调函数处理结果。
- **适用场景**：适用于不需要关心发送结果的业务场景，如日志收集、监控数据上传等。这种方式具有很高的发送速度和吞吐量。
- **示例代码**：
  ```java
  DefaultMQProducer producer = new DefaultMQProducer("producer-group");
  producer.setNamesrvAddr("localhost:9876");
  producer.start();

  Message message = new Message("topic", "tag", "Hello, RocketMQ".getBytes());
  producer.sendOneway(message);

  producer.shutdown();
  ```

### 批量消息（Batch Message）
- **定义**：批量消息是指生产者将多条消息打包成一个消息批次发送到服务器。服务器将消息批次拆分并存储。
- **适用场景**：适用于批量数据处理的业务场景，如批量生成订单、批量用户注册等。批量消息可以提高发送效率和吞吐量，但每个批次的消息大小不能超过4MB。
- **示例代码**：
  ```java
  DefaultMQProducer producer = new DefaultMQProducer("producer-group");
  producer.setNamesrvAddr("localhost:9876");
  producer.start();

  List<Message> messages = new ArrayList<>();
  messages.add(new Message("topic", "tag", "Hello, RocketMQ 1".getBytes()));
  messages.add(new Message("topic", "tag", "Hello, RocketMQ 2".getBytes()));
  messages.add(new Message("topic", "tag", "Hello, RocketMQ 3".getBytes()));

  producer.send(messages);

  producer.shutdown();
  ```

### 总结
- **同步消息**：发送后等待服务器返回结果，适用于对消息可靠性要求较高的场景。
- **异步消息**：发送后立即返回，通过回调函数处理结果，适用于对响应时间有要求的场景。
- **单向消息**：发送后不等待结果也没有回调函数，适用于不关心发送结果的场景。
- **批量消息**：将多条消息打包成一个批次发送，提高发送效率，适用于批量数据处理的场景。

通过不同的消息发送方式，RocketMQ 能够满足多种业务场景的需求，提供灵活的消息传递方案。

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



