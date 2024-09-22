package com.sahil.kitchensink.mapper;

import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MemberToMemberDTO {

    public static MemberDTO map(Member member) {
        MemberDTO result = new MemberDTO();
        result.setId(member.getId());
        result.setName(member.getName());
        result.setEmail(member.getEmail());
        result.setPhoneNumber(member.getPhoneNumber());

        return result;
    }
}
