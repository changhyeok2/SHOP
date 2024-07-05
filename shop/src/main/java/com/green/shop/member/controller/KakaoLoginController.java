package com.green.shop.member.controller;

import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.service.KakaoLoginService;
import com.green.shop.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KakaoLoginController {

    @Value("${kakao.default.password}")
    private String kakaoPassword;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthenticationManager authenticationManager;

    //@ResponseBody : 메소드가 리턴하는 결과를 html의 body에 전달
    @GetMapping("/members/kakao")
    public @ResponseBody String kakaoCallback(String code){

        //인증서버로부터 code를 이용하여 엑세스 토큰을 받아옴
        String accessToken = kakaoLoginService.getAccessToken(code);

        //사용자 정보 받아오기
        MemberDto memberInfo = kakaoLoginService.getMemberInfo(accessToken);

        try{
            Long memberId = memberService.findMemberId(memberInfo.getId());
        }catch (Exception e){
            memberService.insertMember(memberInfo);
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberInfo.getId(), kakaoPassword);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";

    }

}
