package com.sahil.kitchensink.controller;


import com.sahil.kitchensink.enums.Roles;
import com.sahil.kitchensink.exception.AuthorizationException;
import com.sahil.kitchensink.mapper.MemberToMemberDTO;
import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.model.markers.CreateMemberCheck;
import com.sahil.kitchensink.model.markers.UpdateMemberCheck;
import com.sahil.kitchensink.model.markers.generalMemberCheck;
import com.sahil.kitchensink.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/members/v1")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<MemberDTO> getAllMembers(@AuthenticationPrincipal UserDetails userDetails) {

        if(isAdmin(userDetails)) {
            return memberService.getAllMembers().stream().map(MemberToMemberDTO::map).toList();
        } else {
            return memberService.getAllMembersforUser(SecurityContextHolder.getContext().getAuthentication().getName())
                    .stream().map(MemberToMemberDTO::map).toList();
        }
    }

    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable(value = "id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberById(id);
        if(isAuthorized(member, userDetails)) {
            return MemberToMemberDTO.map(member);
        } else {
            throw new AuthorizationException("not authorized");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDTO createMember(@RequestBody  @Validated({generalMemberCheck.class, CreateMemberCheck.class}) MemberDTO memberDTO, @AuthenticationPrincipal UserDetails userDetails) {

        // don't map id will be created automatically
        Member member = Member.builder()
                .setName(memberDTO.getName())
                .setEmail(memberDTO.getEmail())
                .setPhoneNumber(memberDTO.getPhoneNumber())
                .setRegisteredBy(userDetails.getUsername())
                .build();
        return MemberToMemberDTO.map(memberService.saveMember(member));

    }

    @PutMapping
    public MemberDTO updateMember(@RequestBody  @Validated({generalMemberCheck.class, UpdateMemberCheck.class}) MemberDTO memberDTO, @AuthenticationPrincipal UserDetails userDetails) {

        var currentMember = memberService.getMemberById(memberDTO.getId());
        if(!isAuthorized(currentMember, userDetails)) {
            throw new AuthorizationException("not authorized");
        }


        var newMember = currentMember.toBuilder()
                .setName(memberDTO.getName())
                .setEmail(memberDTO.getEmail())
                .setPhoneNumber(memberDTO.getPhoneNumber())
                .build();
        return MemberToMemberDTO.map(memberService.saveMember(newMember));

    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable("id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberById(id);
        if(isAuthorized(member, userDetails)) {
            memberService.deleteMember(id);
        } else {
            throw new AuthorizationException("not authorized");
        }
    }

    @DeleteMapping()
    public void deleteAllMembers(@AuthenticationPrincipal UserDetails userDetails) {
        if(isAdmin(userDetails)) {
            memberService.deleteAllMembers();
        } else {
            throw new AuthorizationException("not authorized");
        }
    }


    private boolean isAuthorized(Member member, UserDetails userDetails) {
        if(isAdmin(userDetails)) {
            return true;
        } else {
            return member.getRegisteredBy().equals(userDetails.getUsername());
        }
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.getValue()));
    }

}
