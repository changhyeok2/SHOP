package com.green.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

//스프링 시큐리티에서 사용되는 인증 진입점를 커스터마이징해서 사용하기 위한 클래스
//인증 진입점 : 인증되지 않은 요청이 보호된 리소스에 접근하려고 할 때 호출되는 지점
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //인증이 필요한 리소스에 접근할 때 호출
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
