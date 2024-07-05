package com.green.shop;

import com.green.shop.config.PageHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageHandlerTest {

    @Test
    @DisplayName("게시글 개수 250개")
    public void test1() {
        PageHandler ph1 = new PageHandler(250, 10, 1);

        //시작 페이지  1
        //마지막 페이지 10
        System.out.println("ph1 : " + ph1);

        assertThat(ph1.getTotalPage()).isEqualTo(25);
        assertThat(ph1.getBeginPage()).isEqualTo(1);
        assertThat(ph1.getEndPage()).isEqualTo(10);
        assertThat(ph1.isFirstPage()).isTrue();
        assertThat(ph1.isLastPage()).isFalse();
    }

    @Test
    @DisplayName("게시글 개수 250개, 현재 페이지 번호 11")
    public void test2() {
        PageHandler ph2 = new PageHandler(250, 10, 11);

        //시작 페이지  1
        //마지막 페이지 10
        System.out.println("ph2 : " + ph2);

        assertThat(ph2.getTotalPage()).isEqualTo(25);
        assertThat(ph2.getBeginPage()).isEqualTo(11);
        assertThat(ph2.getEndPage()).isEqualTo(20);
        assertThat(ph2.isFirstPage()).isFalse();
        assertThat(ph2.isLastPage()).isFalse();
    }

    @Test
    @DisplayName("게시글 개수 155개, 현재 페이지 번호 11")
    public void test3() {
        PageHandler ph3 = new PageHandler(155,
                                          10, 11);

        System.out.println("ph3 : " + ph3);

        assertThat(ph3.getTotalPage()).isEqualTo(16);
        assertThat(ph3.getBeginPage()).isEqualTo(11);
        assertThat(ph3.getEndPage()).isEqualTo(16);
        assertThat(ph3.isFirstPage()).isFalse();
        assertThat(ph3.isLastPage()).isTrue();
    }


}
