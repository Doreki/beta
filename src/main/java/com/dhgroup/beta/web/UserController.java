package com.dhgroup.beta.web;

import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final MemberService memberService;

    @Autowired
    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/v1/user/1h2g2yysh297h2s")
    public Long singUp(MemberCreateDto memberCreateDto) {
        return memberService.signUp(memberCreateDto);
    }
}
