package com.green.shop;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.service.MemberSecurityService;
import com.green.shop.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberSecurityService memberSecurityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 테스트")
    public void insertMemberTest() {
        MemberDto member = new MemberDto();
        member.setId("test2");
        member.setPassword("1234");
        member.setName("이자바");
        member.setEmail("test2@naver.com");
        member.setAddress("부산");
        member.setRole(Role.USER);

        System.out.println(member);
        int result = memberService.insertMember(member);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("중복 테스트")
    public void overlapTest() {
        MemberDto memberDto = new MemberDto();

        memberDto.setId("test22");
        memberDto.setEmail("test2@naver.com");

        //람다식 사용 X
        Throwable th = null;

        try{
            memberService.insertMember(memberDto);
        } catch (IllegalStateException e) {
            th = e;
        }
        System.out.println(th.getMessage());
        assertThat(th.getMessage()).isIn("중복된 아이디", "이미 가입한 회원");

        //람다식 사용
        Throwable th2 = assertThrows(IllegalStateException.class,
                                () -> memberService.insertMember(memberDto));

        System.out.println(th2.getMessage());

        assertThat(th2.getMessage()).isIn("중복된 아이디", "이미 가입한 회원");
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginMemberTest() throws Exception{
        String id="1";
        String password="11111111";

        //로그인 요청을 모방
        mockMvc.perform(formLogin().userParameter("id") //로그인 시 사용자의 이름을 나타내는 부분
                .loginProcessingUrl("/members/login")   //로그인을 처리할 url주소
                .user(id).password(password))   //로그인 요청에 사용될 아이디와 비번을 설정
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //로그인을 성공하고 사용자가 인증되었는지 검증

        //로그아웃 요청을 보냄
        mockMvc.perform(MockMvcRequestBuilders.get(
                                    "/members/logout"))
                .andExpect(status().is3xxRedirection());

        //로그아웃 후, 로그인되지 않은 상태인지 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}
