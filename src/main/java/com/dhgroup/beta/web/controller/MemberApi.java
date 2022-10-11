package com.dhgroup.beta.web.controller;

import com.dhgroup.beta.aop.annotation.LogAspect;
import com.dhgroup.beta.aop.annotation.ValidAspect;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.exception.OverlapMemberException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.CMResponseDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberUpdateRequest;
import com.dhgroup.beta.web.dto.MemberDto.MemberUpdateResponse;
import com.dhgroup.beta.web.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberApi {

    private final MemberService memberService;


    @LogAspect
    @ValidAspect
    @PostMapping("/1h2g2yysh297h2s")
    public ResponseEntity<?> singUp(@Validated(ValidationSequence.class) @RequestBody MemberRequestDto memberRequestDto, BindingResult bindingResult) {

        if(memberService.isDuplicated(memberRequestDto.getGoogleId()))//중복 회원
            throw new OverlapMemberException("이미 존재하는 회원입니다.");

        Long memberId = memberService.signUp(memberRequestDto);
        return ResponseEntity
                .status(CREATED)
                .body(CMResponseDto.createCMResponseDto(1,"회원가입에 성공 하셨습니다.",memberId));
    }

    @GetMapping("/login/{googleId}")
    public ResponseEntity<?> logIn(@PathVariable String googleId) {
        MemberResponseDto memberResponseDto = memberService.logIn(googleId);
        return ResponseEntity
                .ok(CMResponseDto.createCMResponseDto(1, "성공적으로 로그인이 되었습니다.", memberResponseDto));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateNickname(@PathVariable Long memberId,
                                            @Validated(ValidationSequence.class)
                                            @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateNickname(memberId, memberUpdateRequest.getNickname());
        Member findMember = memberService.findMemberByMemberId(memberId);
        MemberUpdateResponse response = MemberUpdateResponse.createMemberUpdateResponse(findMember.getId(), findMember.getNickname());

        return ResponseEntity.ok(CMResponseDto.createCMResponseDto(1,"닉네임이 변경되었습니다.",null));
    }

}
