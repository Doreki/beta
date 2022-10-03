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


    @PostMapping("/api/v1/member/1h2g2yysh297h2s")
    public Long singUp(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.isDuplicated(memberRequestDto.getGoogleId()); //중복 회원
        return memberService.signUp(memberRequestDto);
    }

    @PostMapping("/api/v1/member/login")
    public Member logIn(@RequestBody String googleId) {
            return memberService.logIn(googleId);
    }

    @PatchMapping("/api/v1/member/{memberId}")
    public void updateNickname(@PathVariable Long memberId, @RequestBody String nickname) {
        memberService.updateNickname(memberId, nickname);
    }

}
