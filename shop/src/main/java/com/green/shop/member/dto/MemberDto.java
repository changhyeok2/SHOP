package com.green.shop.member.dto;

import com.green.shop.member.constant.OAuthType;
import com.green.shop.member.constant.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {
    private Long memberId;
    private String id;
    private String password;
    private String name;
    private String email;
    private String address;
    private Role role;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private OAuthType oAuth;
}
