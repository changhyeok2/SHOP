package com.green.shop.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartOrderDto {
    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;
}
