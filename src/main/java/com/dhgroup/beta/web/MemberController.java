package com.dhgroup.beta.web;

import com.dhgroup.beta.aop.annotation.ValidAspect;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.exception.OverlapMemberException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberDto.MemberRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import com.dhgroup.beta.web.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;


    @ValidAspect
    @PostMapping("/1h2g2yysh297h2s")
    public ResponseEntity<?> singUp(@Validated(ValidationSequence.class) @RequestBody MemberRequestDto memberRequestDto, BindingResult bindingResult) {

        if(memberService.isDuplicated(memberRequestDto.getGoogleId()))//중복 회원
            throw new OverlapMemberException("이미 존재하는 회원입니다.");

        Long memberId = memberService.signUp(memberRequestDto);
        return ResponseEntity.ok(memberId);
    }

    @GetMapping("/login")
    public MemberResponseDto logIn(@RequestBody MemberRequestDto memberRequestDto) {
            return memberService.logIn(memberRequestDto.getGoogleId());
    }

    @PatchMapping("/{memberId}")
    public void updateNickname(@PathVariable Long memberId, @RequestBody MemberRequestDto memberRequestDto) {
        memberService.updateNickname(memberId, memberRequestDto.getNickname());
    }

}
