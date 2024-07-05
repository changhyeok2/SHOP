package com.green.shop.order.service;

import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.constant.OrderStatus;
import com.green.shop.order.dto.OrderDto;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.dto.OrderItemDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class) //두 개의 테이블을 함께 묶어서 처리해야될 경우 사용(orders, order_item 테이블)
@RequiredArgsConstructor
public class OrderService {

    private final MemberMapper memberMapper;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;

    //주문 처리(form -> orderDto)
    public Long createOrder(OrderForm orderForm, String id) {
        OrderDto orderDto = new OrderDto();

        //로그인 아이디를 이용해서 MemberId를 검색
        MemberDto memberDto = memberMapper.loginMember(id);

        //주문 상태와 아이디를 저장
        orderDto.setOrderStatus(OrderStatus.ORDER);
        orderDto.setMemberId(memberDto.getMemberId());

        //orders테이블에 주문 내용을 추가
        orderMapper.insertOrder(orderDto);

        System.out.println("orderService(orderDto) : " + orderDto);
        System.out.println("orderService(orderId) : " + orderDto.getOrderId());

        orderItemService.createOrderItem(orderDto.getOrderId(), orderForm.getItemId(), orderForm.getCount());

        return orderDto.getOrderId();
    }

    public List<OrderHistDto> orderSelect(Map map){
        return orderMapper.orderSelect(map);
    }

    public int orderCount(Map map){
        return orderMapper.orderCount(map);
    }

    //로그인한 사용자와 주문한 사람이 동일한지 확인
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String id){

        Long loginId = memberMapper.findMemberId(id);

        Long memberId = orderMapper.orderMemberId(orderId);

        if (loginId != memberId){
            return false;
        }
        return true;
    }

    //주문 취소하기
    public Long cancelOrder(Long orderId){
        //주문 상품 정보를 추출
        OrderHistDto orderHistDto = orderMapper.findOrder(orderId);

        orderItemService.addStock(orderHistDto.getItemId(), orderHistDto.getCount());
        orderMapper.cancelOrder(orderId);

        return orderId;
    }

    public Long cartOrders(List<OrderForm> orderFormList, String id){
        MemberDto memberDto = memberMapper.loginMember(id);

        OrderDto orderDto = new OrderDto();
        orderDto.setMemberId(memberDto.getMemberId());
        orderDto.setOrderStatus(OrderStatus.ORDER);

        orderMapper.insertOrder(orderDto);

        //List<OrderItemDto> orderItemDtoList = new ArrayList<>();

        for(OrderForm orderForm : orderFormList){
            orderItemService.createOrderItem(orderDto.getOrderId(), orderForm.getItemId(), orderForm.getCount());
            //orderItemDtoList.add(orderItemDto);
        }

        return orderDto.getOrderId();

    }
}
