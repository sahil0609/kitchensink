package com.sahil.kitchensink.service;

import com.sahil.kitchensink.exception.MemberNotFoundException;
import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }

    public List<Member> getAllMembersforUser(String registeredBy) {
        return memberRepository.finaAllMMembersByRegisteredBy(registeredBy);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public void deleteMember(String id) {
        memberRepository.deleteById(id);
    }

    public void deleteAllMembers() {
        memberRepository.deleteAll();
    }

    public Optional<Member> findMemberByEmail(String email) {
       return memberRepository.findByEmail(email);
    }

}
