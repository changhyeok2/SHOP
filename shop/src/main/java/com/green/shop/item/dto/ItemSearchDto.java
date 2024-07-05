package com.green.shop.item.dto;

import com.green.shop.item.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    //조회 날짜 기준일
    private String searchDateType;

    //판매중 또는 품절
    private ItemSellStatus searchSellStatus;

    //상품명 또는 상세설명
    private String searchBy;

    //검색어
    private String searchText = "";
}
