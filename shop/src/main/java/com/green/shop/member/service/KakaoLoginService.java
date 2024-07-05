package com.green.shop.member.service;

import com.google.gson.Gson;
import com.green.shop.member.constant.OAuthType;
import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoLoginService {

    @Value("${kakao.default.password}")
    private String kakaoPassword;

    public String getAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", "43dd475c7d784b14d804ecc3265ee4b0");
        body.add("redirect_uri", "http://localhost:8080/members/kakao");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        //RestTemplate : 브라우저 없이 http 요청을 처리할 수 있음
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token" //엑세스 토큰 요청 주소
                , HttpMethod.POST //요청방식
                , requestEntity //요청 헤더와 바디
                , String.class); //응답받을 타입

        String jsonData = responseEntity.getBody();

        Gson gsonObj = new Gson();
        Map<?,?> data = gsonObj.fromJson(jsonData, Map.class);

        return (String) data.get("access_token");
    }

    public MemberDto getMemberInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        //RestTemplate : 브라우저 없이 http 요청을 처리할 수 있음
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me" //엑세스 토큰 요청 주소
                , HttpMethod.POST //요청방식
                , requestEntity //요청 헤더와 바디
                , String.class); //응답받을 타입

        String memberInfo = responseEntity.getBody();

        Gson gsonObj = new Gson();
        Map<?,?> data = gsonObj.fromJson(memberInfo, Map.class);

        Double id = (Double) (data.get("id"));

        String nickName = (String) ((Map<?,?>) (data.get("properties"))).get("nickname");
        String email = (String) ((Map<?,?>) (data.get("kakao_account"))).get("email");

        MemberDto memberDto = new MemberDto();
        memberDto.setId(Double.toString(id));
        memberDto.setName(nickName);
        memberDto.setEmail(email);
        memberDto.setPassword(kakaoPassword);
        memberDto.setRole(Role.USER);
        memberDto.setOAuth(OAuthType.KAKAO);

        return memberDto;
    }
}
