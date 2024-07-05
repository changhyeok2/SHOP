package com.green.shop.cart.dto;

import lombok.Data;

@Data
public class CartDetailDto {
    private Long cartItemId;

    private String itemName;

    private int price;

    private int count;

    private String imgUrl;
}
