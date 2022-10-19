package com.dhgroup.beta.web.controller.api;

import com.dhgroup.beta.aop.annotation.LogAspect;
import com.dhgroup.beta.aop.annotation.ValidAspect;
import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.exception.OverlapMemberException;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.CMResponseDto;
import com.dhgroup.beta.web.dto.MemberDto.KakaoJoinRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberUpdateRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberUpdateResponseDto;
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
    @PostMapping("/kakao/1h2g2yysh297h2s")
    public ResponseEntity<?> kakaoLoginAndJoin(@Validated(ValidationSequence.class) @RequestBody KakaoJoinRequestDto kakaoJoinRequestDto, BindingResult bindingResult) {

        String kakaoId = kakaoJoinRequestDto.getAuthId();
        //중복 회원
        if(memberService.isDuplicated(kakaoId, Provider.KAKAO)){
            MemberResponseDto memberResponseDto = memberService.login(kakaoId, Provider.KAKAO);
            return ResponseEntity
                    .ok(CMResponseDto.createCMResponseDto(1, "성공적으로 로그인이 되었습니다.", memberResponseDto));
        } else {
            Long memberId = memberService.join(kakaoJoinRequestDto);
            return ResponseEntity
                    .status(CREATED)
                    .body(CMResponseDto.createCMResponseDto(1,"회원가입에 성공 하셨습니다.",memberId));
        }
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateNickname(@PathVariable Long memberId,
                                            @Validated(ValidationSequence.class)
                                            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.updateNickname(memberId, memberUpdateRequestDto.getNickname());
        Member findMember = memberService.findMemberByMemberId(memberId);
        MemberUpdateResponseDto response = MemberUpdateResponseDto.createMemberUpdateResponse(findMember.getId(), findMember.getNickname());

        return ResponseEntity.ok(CMResponseDto.createCMResponseDto(1,"닉네임이 변경되었습니다.",response));
    }

}
