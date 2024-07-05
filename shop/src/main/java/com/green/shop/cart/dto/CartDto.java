package com.green.shop.cart.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDto {
    private Long cartId;
    private Long memberId;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
