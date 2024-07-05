package com.green.shop;

import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.constant.OrderStatus;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.mapper.OrderMapper;
import com.green.shop.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OrderMapper orderMapper;

    private MemberDto memberDto;
    private ItemDto itemDto;
    private Long orderId;

    int i = 2;

    //테스트 메서드가 실행되기 전에 무조건 자동실행함
    @BeforeEach
    void setUp() throws Exception{
        String uniqueId = "tester" + i;
        String uniqueEmail = "tester" + i + "@naver.com";

        memberDto = new MemberDto();
        memberDto.setId(uniqueId);
        memberDto.setEmail(uniqueEmail);
        memberDto.setPassword("11111111");
        memberDto.setName("김그린");

        memberMapper.insertMember(memberDto);

        System.out.println("memberDto : " + memberDto);

        itemDto = new ItemDto();
        itemDto.setItemName("물건1");
        itemDto.setStockNumber(100);
        itemDto.setItemDetail("설명");
        itemDto.setPrice(10000);
        itemDto.setItemSellStatus(ItemSellStatus.SELL);

        itemMapper.itemInsert(itemDto);

        System.out.println("itemDto : " + itemDto);

        ItemImgDto itemImgDto = new ItemImgDto();
        itemImgDto.setItemId(itemDto.getItemId());
        itemImgDto.setImgName("사진1");
        itemImgDto.setRepImgYn("Y");

        itemMapper.insertItemImg(itemImgDto);

        System.out.println("itemImgDto : " + itemImgDto);

        OrderForm orderForm = new OrderForm();
        orderForm.setItemId(itemDto.getItemId());
        orderForm.setCount(2);

        orderId = orderService.createOrder(orderForm, memberDto.getId());
    }

//    @Test
//    @DisplayName("주문 테스트")
//    public void order() throws Exception {
//        OrderForm orderForm = new OrderForm();
//        orderForm.setItemId(36L); //db확인
//        orderForm.setCount(5);
//
//        Long orderId = orderService.createOrder(orderForm, "admin"); //db확인
//    }

    @Test
    void cancelOrderTest() throws Exception{
        //테스트를 수행하기 위해 H2 데이터베이스 사용하는 경우가 많음

        int beforeCount = itemDto.getStockNumber();

        Long canceledOrderId = orderService.cancelOrder(orderId);
        assertThat(canceledOrderId).isNotNull();

        OrderHistDto orderHistDto = orderMapper.findOrder(canceledOrderId);
        assertThat(OrderStatus.CANCEL).isEqualTo(orderHistDto.getOrderStatus());
        assertThat(itemDto.getStockNumber()).isEqualTo(beforeCount);
    }
}
