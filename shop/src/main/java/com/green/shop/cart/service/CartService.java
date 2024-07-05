package com.green.shop.cart.service;

import com.green.shop.cart.dto.CartDto;
import com.green.shop.cart.dto.CartDetailDto;
import com.green.shop.cart.dto.CartItemDto;
import com.green.shop.cart.dto.CartOrderDto;
import com.green.shop.cart.form.CartForm;
import com.green.shop.cart.mapper.CartMapper;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CartService {

    private final CartMapper cartMapper;
    private final MemberMapper memberMapper;
    private final ItemMapper itemMapper;
    private final OrderService orderService;

    //장바구니를 추가
    public Long addCart(CartForm cartForm, String id){
        //로그인한 아이디로 memberId를 검색
        Long memberId = memberMapper.findMemberId(id);

        if (memberId == null){
            throw new IllegalArgumentException("id를 찾을 수 없습니다.");
        }

        //로그인한 사용자의 장바구니가 있는지 확인
        CartDto cart = createCartIfNotExists(memberId);

        //제품 추가
        return addItemToCart(cart, cartForm);
    }

    //장바구니가 존재하는지 확인
    private CartDto createCartIfNotExists(Long memberId){
        CartDto cart = cartMapper.findMemberCart(memberId);

        //memberId로 장바구니를 검색했는데 결과가 null이면(장바구니 존재X)
        if (cart == null){
            CartDto cartDto = new CartDto();
            cartDto.setMemberId(memberId);
            //장바구니 생성
            cartMapper.insertCart(cartDto);

            //새로 생성한 장바구니를 조회해서 저장
            cart = cartMapper.findMemberCart(memberId);
        }

        return cart;
    }

    //장바구니에 상품을 추가
    private Long addItemToCart(CartDto cart, CartForm cartForm){
        ItemDto item = itemMapper.selectItem(cartForm.getItemId());

        Map<String, Object> map = new HashMap<>();

        map.put("cartId", cart.getCartId());
        map.put("itemId", item.getItemId());

        CartItemDto cartItemDto = cartMapper.findItemInCart(map);

        if (cartItemDto == null){
            cartItemDto = new CartItemDto();
            cartItemDto.setCartId(cart.getCartId());
            cartItemDto.setItemId(item.getItemId());
            cartItemDto.setCount(cartForm.getCount());

            cartMapper.insertCartItem(cartItemDto);
        }else{
            cartItemDto.setCount(cartItemDto.getCount() + cartForm.getCount());
            map.put("count",cartItemDto.getCount());
            map.put("cartItemId", cartItemDto.getCartItemId());
            cartMapper.updateCount(map);
        }

        return cartItemDto.getCartId();
    }

    public List<CartDetailDto> getCartList(String id){
        Long memberId = memberMapper.findMemberId(id);

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        //로그인한 회원의 장바구니를 조회
        CartDto cart = cartMapper.findMemberCart(memberId);

        //장바구니에 상품이 없으면 비어있는 리스트를 반환
        if (cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartMapper.findCartDetail(cart.getCartId());

        return cartDetailDtoList;
    }

    //로그인한 회원과 장바구니의 소유주가 같은 사람인지 확인
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String id){
        Long loginMemberId = memberMapper.findMemberId(id);

        Long curMemberId = cartMapper.findMemberId(cartItemId);

        if(loginMemberId != curMemberId){
            return false;
        }
        return true;
    }




    //화면에서 변경한 상품수를 db에도 적용
    public void updateCartItemCount(Long cartItemId, int count){

        CartItemDto cartItemDto = cartMapper.findCartItem(cartItemId);

        if (cartItemDto == null){
            throw  new IllegalArgumentException("상품이 존재하지 않습니다.");
        }

        Map map = new HashMap();
        map.put("count",count);
        map.put("cartItemId",cartItemId);

        cartMapper.updateCount(map);
    }





    //장바구니 삭제
    public void deleteCartItem(Long cartItemId) {

        CartItemDto cartItemDto = cartMapper.findCartItem(cartItemId);

        if(cartItemDto == null){
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }

        cartMapper.deleteCartItem(cartItemId);

//        if (cartMapper.deleteCartItem(cartItemId) == 0) {
//            throw new IllegalArgumentException("삭제할 항목이 없습니다.");
//        }
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String id){
        List<OrderForm> orderFormList = new ArrayList<>();

        //장바구니 물건 삭제
        for(CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItemDto cartItemDto = cartMapper.findCartItem(cartOrderDto.getCartItemId());

            OrderForm orderForm = new OrderForm();

            orderForm.setItemId(cartItemDto.getItemId());
            orderForm.setCount(cartItemDto.getCount());

            orderFormList.add(orderForm);
        }

        Long orderId = orderService.cartOrders(orderFormList, id);

        for(CartOrderDto cartOrderDto1 : cartOrderDtoList){
            cartMapper.deleteCartItem(cartOrderDto1.getCartItemId());
        }

        return orderId;

    }
}
