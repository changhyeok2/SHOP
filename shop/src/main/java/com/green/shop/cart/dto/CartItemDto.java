package com.green.shop.cart.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDto {
    private Long cartItemId;
    private Long cartId;
    private Long itemId;
    private int count;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
