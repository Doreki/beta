package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotFoundGoogleIdException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/api/v1/member/1h2g2yysh297h2s")
    public Long singUp(MemberRequestDto memberRequestDto) {
        return memberService.signUp(memberRequestDto);
    }

    @PostMapping("/api/v1/member/login")
    public Member signIn(String googleId) {
        if(memberService.memberCheck(googleId))  //구글 아이디가 db에 있으면 로그인 없으면 회원가입창으로 가기위한 에러메시지 출력
            return memberService.logIn(googleId);
        else
            throw new NotFoundGoogleIdException();
    }


}
