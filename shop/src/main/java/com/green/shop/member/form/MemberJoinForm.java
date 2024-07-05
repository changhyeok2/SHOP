package com.green.shop.member.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberJoinForm {

    @NotBlank(message = "아이디를 필수 입력값입니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

}
