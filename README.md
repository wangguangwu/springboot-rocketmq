# rocketmq-demo

项目地址：[rocketmq-demo](https://github.com/wangguangwu/springboot-rocketmq)

本项目由以下模块组成：

1. MQ 简介
2. RocketMQ 简介
3. Docker 启动 RocketMQ
4. SpringBoot 集成 RocketMQ

# 1. MQ 简介

## 1.1 MQ 的基本概念

### 1.1.1 MQ 的定义

消息队列（Message Queue，简称 MQ）是一种通信方式，用于不同的应用程序之间通过消息传递进行异步通信。它通过提供一个消息缓冲区，使得发送方和接收方在不同时间完成数据传输，从而实现系统解耦和异步处理。

### 1.1.2 MQ 的类型

#### 1.1.2.1 点对点（Point-to-Point）模式

消息从一个生产者发送到一个特定的消费者。消息被存储在队列中，消费者从队列中获取消息进行处理。

#### 1.1.2.2 发布/订阅（Publish/Subscribe）模式

生产者将消息发布到一个主题，多个消费者订阅该主题，每个订阅者都可以接收到消息。

## 1.2 MQ 的作用

1. **解耦**：通过消息队列，生产者和消费者可以独立演化，降低系统耦合度。
2. **异步处理**：提高系统吞吐量，生产者在发送消息后可以立即返回，无需等待消费者处理完毕。
3. **削峰填谷**：通过缓冲消息，平滑处理突发流量，避免系统过载。
4. **数据持久化**：提供消息持久化能力，确保消息不丢失，提升系统可靠性。

## 1.3 MQ 的优缺点

### 1.3.1 MQ 的优点

1. **提高系统性能**：通过异步处理和并行消费，提高系统整体处理能力。
2. **提高系统可靠性**：通过消息持久化机制，保证消息不会因系统崩溃而丢失。
3. **灵活性和可扩展性**：方便系统模块的拆分和重组，支持系统的水平扩展。

### 1.3.2 MQ 的缺点

1. **复杂性增加**：引入 MQ 后，需要处理消息的重复消费、消息顺序、消息丢失等问题。
2. **运维成本**：需要维护 MQ 集群的稳定性和性能，增加了运维工作量。
3. **延迟**：消息传递过程中的网络延迟和 MQ 系统的处理时间可能会引入延迟。

## 1.4 MQ 的组件

### 1.4.1 生产者（Producer）

负责创建和发送消息到 MQ 系统。

生产者是负责创建和发送消息到消息队列的角色。生产者可以根据业务需求，选择不同的消息发送模式，如同步消息、异步消息和单向消息。

### 1.4.2 消费者（Consumer）

负责从 MQ 系统接收和处理消息。

消费者是负责从消息队列中接收和处理消息的角色。消费者可以订阅特定的主题或队列，从而接收相应的消息并进行处理。

### 1.4.3 消息（Message）

数据传递的载体，包含消息体和消息属性。

消息是数据传递的载体，包含消息头、消息体和消息属性。消息头用于存储元数据（如消息ID、时间戳等），消息体存储实际的数据内容，消息属性存储一些可选的键值对。

### 1.4.4 队列（Queue）/主题（Topic）

消息的存储和传递通道，队列用于点对点通信，主题用于发布/订阅模式。

- **队列（Queue）**：用于点对点模式，消息被存储在队列中，每个消息只能被一个消费者消费。
- **主题（Topic）**：用于发布/订阅模式，消息被发布到主题，所有订阅该主题的消费者都可以接收到消息。

### 1.4.5 Broker

MQ 系统的核心组件，负责接收、存储和转发消息。

Broker 是消息队列系统的核心组件，负责接收、存储和转发消息。Broker 管理消息的持久化和传输，确保消息的可靠性和高可用性。

### 1.4.6 NameServer

用于管理和发现 Broker 的服务。

NameServer 是用于管理和发现 Broker 的服务。它维护 Broker 的元数据信息，帮助生产者和消费者找到对应的 Broker 进行消息传递。

### 1.4.7 消息存储（Storage）

负责持久化存储消息，保证消息在系统崩溃后的可恢复性。

消息存储负责将消息持久化到磁盘，确保消息在系统崩溃后的可恢复性。高效的消息存储机制可以提高系统的吞吐量和可靠性。

### 1.4.8 消息路由（Routing）

管理消息在生产者、Broker 和消费者之间的路由。

消息路由负责管理消息在生产者、Broker 和消费者之间的传递路径。通过合理的路由策略，可以优化消息传递的性能和可靠性。

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

一个 `topic` 代表一个消息类别，你可以为不同类型的消息创建不同的 `topic`。生产者根据消息类型发送到不同的 `topic`
，消费者订阅相应的 `topic` 来接收特定类型的消息。

**生产者代码示例：**

```java
DefaultMQProducer producer = new DefaultMQProducer("producer-group");
producer.

setNamesrvAddr("localhost:9876");
producer.

start();

Message message1 = new Message("topicA", "tagA", "keyA", "Hello topicA".getBytes());
producer.

send(message1);

Message message2 = new Message("topicB", "tagB", "keyB", "Hello topicB".getBytes());
producer.

send(message2);

producer.

shutdown();
```

**消费者代码示例：**

```java
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group");
consumer.

setNamesrvAddr("localhost:9876");
consumer.

subscribe("topicA","*");

consumer.

registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage (List < MessageExt > msgs, ConsumeConcurrentlyContext context){
        for (MessageExt msg : msgs) {
            System.out.println(new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});

        consumer.

start();
```

### 2. 根据 Tag 分类

`Tag` 是消息的二级分类，可以在一个 `topic` 下进一步对消息进行分类。这样消费者可以根据 `tag` 进行过滤，接收特定类别的消息。

**生产者代码示例：**

```java
Message message1 = new Message("topicA", "tagA", "keyA", "Hello tagA".getBytes());
producer.

send(message1);

Message message2 = new Message("topicA", "tagB", "keyB", "Hello tagB".getBytes());
producer.

send(message2);
```

**消费者代码示例：**

```java
consumer.subscribe("topicA","tagA"); // 只订阅 tagA 的消息

consumer.

registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage (List < MessageExt > msgs, ConsumeConcurrentlyContext context){
        for (MessageExt msg : msgs) {
            System.out.println(new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

### 3. 根据 Key 分类

`Key` 用于标识消息，可以用作消息的唯一标识符或用于特定业务逻辑处理。虽然 `key`
不能直接用于消息过滤，但可以在消费者接收到消息后，通过 `key` 进行业务逻辑分类。

**生产者代码示例：**

```java
Message message1 = new Message("topicA", "tagA", "keyA", "Hello keyA".getBytes());
producer.

send(message1);

Message message2 = new Message("topicA", "tagA", "keyB", "Hello keyB".getBytes());
producer.

send(message2);
```

**消费者代码示例：**

```java
consumer.subscribe("topicA","*"); // 订阅 topicA 下所有 tag 的消息

consumer.

registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage (List < MessageExt > msgs, ConsumeConcurrentlyContext context){
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
message1.

putUserProperty("a","1");
producer.

send(message1);

Message message2 = new Message("topicA", "tagA", "keyB", "Hello SQL filter".getBytes());
message2.

putUserProperty("a","2");
producer.

send(message2);
```

**消费者代码示例：**

```java
consumer.subscribe("topicA",MessageSelector.bySql("a = 1"));

        consumer.

registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage (List < MessageExt > msgs, ConsumeConcurrentlyContext context){
        for (MessageExt msg : msgs) {
            System.out.println("Processing message with property a = 1: " + new String(msg.getBody()));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

以上是几种在 RocketMQ 中对消息进行分类和过滤的方法，可以根据具体的业务需求选择合适的方案。

# SpringBoot 集成 RocketMQ



