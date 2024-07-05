package com.green.shop.item.form;

import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemImgDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemForm {

    private Long id;

    @NotBlank(message = "상품명을 입력하세요")
    private String itemName;

    @NotNull(message = "가격을 입력하세요")
    private Integer price;

    @NotBlank(message = "상세 설명을 입력하세요")
    private String itemDetail;

    @NotNull(message="재고를 입력하세요")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

}
