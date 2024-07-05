package com.green.shop.order.service;

import com.green.shop.exception.OutOfStockException;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.order.dto.OrderItemDto;
import com.green.shop.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) //두 개의 테이블을 함께 묶어서 처리해야될 경우 사용(orders, order_item 테이블)
@Service
public class OrderItemService {

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    //상품 주문 처리(order -> orderItemDto)
    public void createOrderItem(Long orderId, Long itemId, int count) {

        OrderItemDto orderItemDto = new OrderItemDto();

        //상품에 해당하는 가격, 재고수량을 가져오기 위해 상품검색
        ItemDto itemDto = itemMapper.selectItem(itemId);

        orderItemDto.setOrderId(orderId);
        orderItemDto.setItemId(itemId);
        orderItemDto.setOrderPrice(this.getTotalPrice(itemDto.getPrice(), count));
        orderItemDto.setCount(count);

        this.removeStock(itemDto, count);

        orderMapper.insertOrderItem(orderItemDto);
    }

    //상품 재고 감소
    public void removeStock(ItemDto itemDto, int stockNumber) {
        int restStock = itemDto.getStockNumber() - stockNumber;

        if (restStock < 0) {
            //예외발생
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : " + itemDto.getStockNumber() + ")");
        }

        Map map = new HashMap();

        map.put("itemId", itemDto.getItemId());
        map.put("stockNumber", restStock);

        orderMapper.changeStock(map);
    }

    //상품취소로 인한 상품 재고 추가
    public void addStock(Long itemId, int stockNumber){

        //상품 조회
        ItemDto itemDto = itemMapper.selectItem(itemId);

        int restStock = itemDto.getStockNumber() + stockNumber;

        Map map = new HashMap();
        map.put("itemId", itemDto.getItemId());
        map.put("stockNumber",restStock);

        orderMapper.changeStock(map);

    }

    //상품 가격 계산
    public int getTotalPrice(int price, int count) {
        return price * count;
    }
}
