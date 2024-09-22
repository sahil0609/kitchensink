package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.repository.MemberRepository;
import com.sahil.kitchensink.service.MemberService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateMemberValidation implements ConstraintValidator<UpdateMemberUniqunessConstraint, MemberDTO> {

    private final MemberService memberService;

    @Override
    public boolean isValid(MemberDTO memberDTO, ConstraintValidatorContext constraintValidatorContext) {
        Member member = memberService.getMemberById(memberDTO.getId());

        if(!memberDTO.getEmail().equals(member.getEmail())) {
            return memberService.findMemberByEmail(memberDTO.getEmail()).isEmpty();
        }
        return true;
    }
}
