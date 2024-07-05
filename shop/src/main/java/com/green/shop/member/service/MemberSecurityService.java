package com.green.shop.member.service;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor //클래스에서 사용할 필수 생성자를 자동으로 생성(final, @NonNull)
public class MemberSecurityService implements UserDetailsService {

    private final MemberMapper memberMapper;

    //사용자 이름을 사용하여 사용자 정보를 읽어오고 UserDetails 객체로 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //사용자의 이름을 이용하여 db에 조회
        MemberDto member = memberMapper.loginMember(username);

        //회원정보가 없으면 예외 발생
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        //사용자의 권한을 나타내기위해 list 생성
        //GrantedAuthority : 사용자가 가지는 권한을 나타내는 인터페이스
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("ADMIN".equals(member.getRole().toString())) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.toString()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        }

        System.out.println(member.getPassword());

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
