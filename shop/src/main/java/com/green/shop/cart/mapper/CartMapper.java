package com.green.shop.cart.mapper;

import com.green.shop.cart.dto.CartDto;
import com.green.shop.cart.dto.CartDetailDto;
import com.green.shop.cart.dto.CartItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartMapper {

    int insertCart(CartDto cartDto);

    int insertCartItem(CartItemDto cartItemDto);

    CartDto findMemberCart(Long memberId);

    CartItemDto findItemInCart(Map map);

    int updateCount(Map map);

    List<CartDetailDto> findCartDetail(Long cartId);

    Long findMemberId(Long cartItemId);

    CartItemDto findCartItem(Long cartItemId);

    int deleteCartItem(Long cartItemId);
}
