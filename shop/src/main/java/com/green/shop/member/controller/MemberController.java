package com.green.shop.member.controller;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.form.MemberJoinForm;
import com.green.shop.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/members")
@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/new")
    public String memberForm(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        System.out.println(token.getHeaderName() + " = " +token.getToken());

        model.addAttribute("memberJoinForm", new MemberJoinForm());
        return "member/memberJoinForm";
    }

    @PostMapping("/new")
    public String newMember(@Valid MemberJoinForm memberJoinForm,
                            BindingResult bindingResult,
                            Model model,

                            RedirectAttributes rttr) {
        //폼에 입력된 데이터의 유효성 검사를 실시

        //유효성 검사에서 오류가 있다면
        //회원가입 페이지로 이동

        if (bindingResult.hasErrors()) {
            return "member/memberJoinForm";
        }

        //오류가 없다면
        //폼에 입력된 내용을 가져옴
        try {
            MemberDto dto = new MemberDto();
            dto.setId(memberJoinForm.getId());
            dto.setPassword(memberJoinForm.getPassword());
            dto.setName(memberJoinForm.getName());
            dto.setEmail(memberJoinForm.getEmail());
            dto.setAddress(memberJoinForm.getAddress());
            dto.setRole(Role.USER);

            //가져온 내용을 service를 이용하여 회원가입을 진행
            memberService.insertMember(dto);

            //model을 이용하여 데이터를 전달하면
            //redirect에서는 메세지가 유지되지 않음.

            //model.addAttribute("joinMessage", "회원가입을 환영합니다.");
            rttr.addFlashAttribute("joinMessage", "회원가입을 환영합니다.");
            System.out.println(memberJoinForm.toString());
        } catch(IllegalStateException e) {

            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberJoinForm";
        }

        //회원가입 시에 중복된 이메일 또는 아이디가 잇으면
        //회원가입 페이지로 이동

        //회원가입에 성공하면 메인페이지로 이동
        return "redirect:/";

    }

    @GetMapping("/login")
    public String loginForm() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value="/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg",
                        "아이디 또는 비밀번호를 확인하세요");

        return "/member/memberLoginForm";

    }
}
