package com.green.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//개발자가 작성한 클래스를 bean으로 등록하고자 할 때 사용
//WebMvcConfigurer : springMVC 구성을 개발자가 직접 구성하도록 설정
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //application.yml 설정파일에서 값을 주입받을 때 사용
    @Value("${uploadPath}")
    String uploadPath;

    //정적 리소스에 대한 요청을 처리하는 방법을 설정
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //브라우저에 입력하는 url이 /images로 시작하는 경우에
        //uploadPath에 설정한 폴더를 기준으로 파일을 읽어옴
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
