package com.green.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//해당 파일이 스프링의 환경 설정 파일
@Configuration
@EnableWebSecurity      //스프링 시큐리티를 활성화시킴
public class SecurityConfig {

    //스프링 시큐리티 필터의 체인을 구성하는 역할
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //httpSecurity 객체를 받아서 처리
        //authorizeHttpRequests : HttpSecurity 객체를 사용해서 요청에 대한 인가를 설정
        //requestMatchers : 특정한 요청을 매칭하기 위해 사용
        //AntPathRequestMatcher("/**") : 경로를 지정(모든 경로에 대하여)
        //permitAll() : 모든 사용자에게 요청을 허용한다.

        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .requestMatchers("/", "/members/**", "/item/**", "/chart/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()) //나머지는 모두 인증 요구

            //1. csrf 토큰을 사용하지 않도록 설정
            .csrf((csrf) -> csrf
                    .ignoringRequestMatchers(
                            new AntPathRequestMatcher("/**")))

            //2. csrf 토큰을 쿠키에 저장하고, HTTP에서 사용할수 있도록 설정
            //.csrf((csrf) -> csrf
            //        .csrfTokenRepository(
            //                CookieCsrfTokenRepository.withHttpOnlyFalse()))

            //로그인 경로 등록
            .formLogin((formLogin) -> formLogin
                    .loginPage("/members/login") //로그인 페이지 url설정
                    .defaultSuccessUrl("/", true) //로그인 성공시 이동할 url
                    .usernameParameter("id")   //로그인시 사용할 파라미터 이름
                    .failureUrl("/members/login/error")) //로그인 실패시 이동할 url

            //로그아웃 처리
            .logout((logout) -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url설정
                    .logoutSuccessUrl("/")  //로그아웃 성공시 이동할 url
                    .invalidateHttpSession(true))  //세션 종료

            //인증되지 않은 사용자가 리소스에 접근하면 CustomAuthenticationEntryPoint실행
            .exceptionHandling((exception) -> exception
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
        ;

        //필터 체인을 리턴
        return http.build();
    }

    //PasswordEncoder는 BCryptPasswordEncoder의 인터페이스
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager : 스프링 시큐리티에서 인증을 처리하는 인터페이스
    //authenticationConfiguration : 스프링 시큐리티에서 인증 구성을 담당하는 클래스
    //인증과 관련된 설정을 가져옴
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
