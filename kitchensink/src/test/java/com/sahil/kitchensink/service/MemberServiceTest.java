package com.sahil.kitchensink.service;

import com.sahil.kitchensink.exception.MemberNotFoundException;
import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(MemberService.class)
class MemberServiceTest {

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    void saveMember_ReturnsSavedMember() {
        Member member = new Member();
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.saveMember(member);

        assertEquals(member, result);
    }

    @Test
    void getMemberById_ReturnsMember_WhenIdExists() {
        String id = "123";
        Member member = new Member();
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(id);

        assertEquals(member, result);
    }

    @Test
    void getMemberById_ThrowsException_WhenIdDoesNotExist() {
        String id = "nonexistent";
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(id));
    }

    @Test
    void getAllMembersforUser_ReturnsMembersList() {
        String registeredBy = "user123";
        List<Member> members = Collections.singletonList(new Member());
        when(memberRepository.finaAllMMembersByRegisteredBy(registeredBy)).thenReturn(members);

        List<Member> result = memberService.getAllMembersforUser(registeredBy);

        assertEquals(members, result);
    }

    @Test
    void getAllMembers_ReturnsAllMembers() {
        List<Member> members = Collections.singletonList(new Member());
        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.getAllMembers();

        assertEquals(members, result);
    }

    @Test
    void deleteMember_DeletesMember() {
        String id = "123";
        doNothing().when(memberRepository).deleteById(id);

        memberService.deleteMember(id);

        verify(memberRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAllMembers_DeletesAllMembers() {
        doNothing().when(memberRepository).deleteAll();

        memberService.deleteAllMembers();

        verify(memberRepository, times(1)).deleteAll();
    }
}