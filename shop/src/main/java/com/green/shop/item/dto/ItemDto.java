package com.green.shop.item.dto;

import com.green.shop.item.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long itemId;

    private String itemName;

    private Integer price;

    private Integer stockNumber;

    private String itemDetail;

    private ItemSellStatus itemSellStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
