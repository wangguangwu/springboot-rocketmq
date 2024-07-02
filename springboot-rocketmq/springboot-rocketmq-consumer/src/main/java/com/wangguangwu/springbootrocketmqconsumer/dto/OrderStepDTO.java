package com.wangguangwu.springbootrocketmqconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顺序消费信息实体类
 *
 * @author wangguangwu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStepDTO {

    private long orderId;

    private String desc;

}
