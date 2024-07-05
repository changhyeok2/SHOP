package com.green.shop.order.controller;

import com.green.shop.config.PageHandler;
import com.green.shop.member.service.MemberService;
import com.green.shop.order.dto.OrderDto;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.mapper.OrderMapper;
import com.green.shop.order.service.OrderService;
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
//@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    //@ResponseBody : 뷰가 아니라 데이터를 전송하고자 할 때 사용(생략 가능)
    //리턴타입 ResponseEntity : 반환값에 HTTP상태 코드와 응답 메시지를 전달하고자 할 때 사용
    //@RequestBody : HTTP 요청에 대한 본문 내용을 자바 객체로 변환

    //Principal principal : 현재 로그인된 유저의 정보를 얻기 위해서는
    //@Controller 어노테이션이 선언된 클래스의
    //메서드의 매개변수로 Principal 객체를 받아오면 직접 접근 가능
    
    //@ResponseBody를 생략하고 @Controller를 써도 동작하는 이유 :
    //메시지 컨버터를 이용하기 때문
    //메시지 컨버터 : @Controller를 사용해서 메서드를 정의, 반환값을 객체로 반환하면
    // 스프링 컨테이너가 자동으로 HTTP 응답 바디로 변환을 수행함
    @PostMapping(value = "/order")
    public ResponseEntity order(@RequestBody @Valid OrderForm orderForm,
                                              BindingResult bindingResult,
                                              Principal principal){

        //오류 검사 후 오류를 저장
        if(bindingResult.hasErrors()){

            //StringBuilder : 변경이 가능한 String
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            //오류에 대한 메시지가 오기때문에 <String>
            //오류의 내용과 HTTP상태 코드를 저장하여 결과를 리턴
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        //스프링 시큐리티에서 제공 객체
        //로그인이 되어 있는 상태라면 로그인 계정에 대한 정보를 담고 있음
        //getName = id
        String id = principal.getName();

        Long orderId;

        try{
            //아이디를 이용해서 memberId를 찾기
            orderId = orderService.createOrder(orderForm, id);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        //주문Id와 HTTP의 상태코드(성공)를 응답
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value={"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable(value = "page", required = false) Integer page,
                            @ModelAttribute("orderHistDto") OrderHistDto orderHistDto,
                            Principal principal, Model model){

        int ps = 4;
        if (page == null) page = 1;

        Map map = new HashMap();
        map.put("page",page * ps - ps);
        map.put("pageSize", ps);

        Long memberId = memberService.findMemberId(principal.getName());

        map.put("memberId",memberId);

        List<OrderHistDto> orderHist = orderService.orderSelect(map);

        int totalCnt = orderService.orderCount(map);

        PageHandler pageHandler = new PageHandler(totalCnt, ps, page);

        model.addAttribute("page", page);
        model.addAttribute("orderHist", orderHist);
        model.addAttribute("pageHandler", pageHandler);

        return "order/orderHist";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId,
                                      Principal principal){

        //FORBIDDEN : HTTP 상태코드(403), 접근 권한이 없음을 의미
        if (!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        try{
            orderService.cancelOrder(orderId);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
