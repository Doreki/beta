package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/api/v1/member/1h2g2yysh297h2s")
    public Long singUp(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.isDuplicated(memberRequestDto.getGoogleId()); //중복 회원
        return memberService.signUp(memberRequestDto);
    }

    @GetMapping("/api/v1/member/login")
    public Member logIn(@RequestBody String googleId) {
            return memberService.logIn(googleId);
    }

    @PatchMapping("/api/v1/member/{memberId}")
    public void updateNickname(@PathVariable Long memberId,
                               @RequestBody
                               @Valid
                               @Pattern(regexp = "^[0-9a-zA-Z가-힇]{2,10}$",message = "특수문자, 공백을 제외하고 2자 이상 10자 이하로 입력하세요.")
                               String nickname) {
        memberService.updateNickname(memberId, nickname);
    }

}
