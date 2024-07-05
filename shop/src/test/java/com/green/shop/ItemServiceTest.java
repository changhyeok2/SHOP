package com.green.shop;

import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        ItemDto item = new ItemDto();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품의 상세 설정");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        //int result = itemService.itemInsert(item);
        //assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 목록 조회 테스트")
    public void itemListAllTest() {
        List<ItemDto> itemList = itemService.itemListAll();
        System.out.println(itemList);
        assertThat(itemList).isNotEmpty();
    }
}
