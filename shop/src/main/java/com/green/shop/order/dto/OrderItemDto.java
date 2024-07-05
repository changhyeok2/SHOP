package com.green.shop.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemDto {

    private Long orderItemId;

    private Long orderId;

    private Long itemId;

    private int orderPrice;

    private int count;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
