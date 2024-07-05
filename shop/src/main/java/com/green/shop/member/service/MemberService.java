package com.green.shop.member.service;

import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public int insertMember(MemberDto memberDto) {

        overlapId(memberDto.getId());
        overlapEmail(memberDto.getEmail());

        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode
                                            (memberDto.getPassword());

        memberDto.setPassword(encodedPassword);

        return memberMapper.insertMember(memberDto);
    }

    void overlapId(String id) {
        MemberDto findId = memberMapper.overlapId(id);

        if (findId != null) {
            throw new IllegalStateException("중복된 아이디");
        }
    }

    void overlapEmail(String email) {
        MemberDto findEmail =  memberMapper.overlapEmail(email);

        if (findEmail != null) {
            throw new IllegalStateException("이미 가입한 회원");
        }
    }

    public Long findMemberId(String id){
        return memberMapper.findMemberId(id);
    }
}
