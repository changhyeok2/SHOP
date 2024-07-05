package com.green.shop.cart.controller;

import com.green.shop.cart.dto.CartDetailDto;
import com.green.shop.cart.dto.CartOrderDto;
import com.green.shop.cart.form.CartForm;
import com.green.shop.cart.service.CartService;
import com.green.shop.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //아이템에서 장바구니 담기 클릭 시
    @PostMapping("/cart")
    public ResponseEntity order(@RequestBody @Valid CartForm cartForm, BindingResult bindingResult, Principal principal, Model model){
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String id = principal.getName();
        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartForm, id);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String show(Model model, Principal principal) {
        String id = principal.getName();

        List<CartDetailDto> cartItems = cartService.getCartList(id);
        model.addAttribute("cartItems", cartItems);

        return "/cart/cartList";
    }

    @PatchMapping("/cartItem/{cartItemId}") //내용을 변경함
    public ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수량을 변경할 수 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("취소를 할 수 없습니다.",HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }

        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
