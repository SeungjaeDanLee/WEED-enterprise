package com.example.weed.dto;

import com.example.weed.authority.W1001_MemberAuthority;
import com.example.weed.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class W1001_MemberDTO {
    private Long id;
    private String name;
    private String email;
    private W1001_MemberAuthority authority;

    public static W1001_MemberDTO fromEntity(Member member) {
        W1001_MemberDTO memberDto = new W1001_MemberDTO();
        memberDto.setId(member.getId());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setAuthority(member.getAuthority());
        return memberDto;
    }
}


