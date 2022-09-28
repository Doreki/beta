package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
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


    @PostMapping("/api/v1/user/1h2g2yysh297h2s")
    public Long singUp(String googleId) {
        return memberService.signUp(googleId);
    }

    @PostMapping("/api/b1/user/login")
    public Map<String,Object> signIn(String googleId) {
        Map<String, Object> result = new HashMap();

        if(memberCheck(googleId)) {
            Member member = memberService.singIn(googleId);
            result.put("Member",member);
        } //회원가입 로그인 같이 묶어야함
    }

    private boolean memberCheck(Member member) {

        return memberRepository.existsById(member.getId());
    }
}
