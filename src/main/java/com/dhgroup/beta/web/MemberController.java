package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;


    @PostMapping("/api/v1/member/1h2g2yysh297h2s")
    public Long singUp(MemberRequestDto memberRequestDto) {
        memberService.isDuplicated(memberRequestDto.getNickname()); //중복된 닉네임이면 예외 발생
        return memberService.signUp(memberRequestDto);
    }

    @PostMapping("/api/v1/member/login")
    public Member logIn(@RequestBody String googleId) {
            return memberService.logIn(googleId);
    }

    @PatchMapping("/api/v1/member/{id}")
    public void updateNickname(@PathVariable Long id, @RequestBody String googleId) {
        memberService.isDuplicated(googleId); //중복되었으면 예외발생
        memberService.updateNickname(id, googleId);
    }

}
