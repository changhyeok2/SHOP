package com.green.shop.item.dto;

import lombok.Data;

@Data
public class ItemMainDto {

    private Long itemId;

    private String itemName;

    private String itemDetail;

    private String imgUrl;

    private Integer price;
}
