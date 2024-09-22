package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.repository.MemberRepository;
import com.sahil.kitchensink.service.MemberService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UniqueMemberEmailValidator implements ConstraintValidator<UniqueMemberEmail, MemberDTO> {

    @Autowired
    private MemberService memberService;

    @Override
    public boolean isValid(MemberDTO memberDTO, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Member> result = memberService.findMemberByEmail(memberDTO.getEmail());
        return result.isEmpty();
    }
}
