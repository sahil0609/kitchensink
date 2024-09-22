package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UniqueMemberEmailValidatorTest {

    @Mock
    private MemberService memberService;


    @InjectMocks
    private UniqueMemberEmailValidator uniqueMemberEmailValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValid_ReturnsTrue_WhenEmailIsUnique() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("unique@example.com");

        when(memberService.findMemberByEmail(memberDTO.getEmail())).thenReturn(Optional.empty());

        boolean result = uniqueMemberEmailValidator.isValid(memberDTO, null);

        assertTrue(result);
    }

    @Test
    void isValid_ReturnsFalse_WhenEmailIsNotUnique() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("existing@example.com");

        when(memberService.findMemberByEmail(memberDTO.getEmail())).thenReturn(Optional.of(new Member()));

        boolean result = uniqueMemberEmailValidator.isValid(memberDTO, null);

        assertFalse(result);
    }

}