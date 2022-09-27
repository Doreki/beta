package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @Autowired
    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public UserController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @PostMapping("/api/v1/user/1h2g2yysh297h2s")
    public Long singUp(MemberCreateDto memberCreateDto) {
        return memberService.signUp(memberCreateDto);
    }

//    @PostMapping("/api/b1/user/login")
//    public Map<String,Object> signIn(String googleId) {
//        Map<String, Object> result = new HashMap()<>;
//
//        if(memberCheck(googleId)) {
//            memberService.signIn(googleId);
//            result.put("nickname",member)
//        }
//        throw new
//    }

//    private boolean memberCheck(String googleId) {
//
//        return memberRepository.existsById(member.getId());
//    }
}
