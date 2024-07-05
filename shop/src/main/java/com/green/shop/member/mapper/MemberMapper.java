package com.green.shop.member.mapper;

import com.green.shop.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int insertMember(MemberDto memberDto);

    MemberDto overlapId(String id);

    MemberDto overlapEmail(String email);

    MemberDto loginMember(String id);

    Long findMemberId(String id);
}
