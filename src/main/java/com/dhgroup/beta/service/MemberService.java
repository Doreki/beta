package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.web.dto.MemberDto.MemberRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(MemberRequestDto memberRequestDto) {

        Member member = memberRequestDto.toEntity();
        member = memberRepository.saveAndFlush(member);
        String nickname = member.getNickname();
        member.updateNickname(nickname);
        return member.getId();
    }

    public MemberResponseDto logIn(String googleId) {
        Member member = findMemberByGoogleId(googleId);
        return MemberResponseDto.createMemberResponseDto(member);
    }


    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = findMemberByMemberId(memberId);
        member.updateNickname(nickname);
    }

    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    public boolean isDuplicated(String googleId) {
        if(memberRepository.existsByGoogleId(googleId))
            return true;
        else
            return false;
    }

    private Member findMemberByGoogleId(String googleId) {
        return memberRepository.findByGoogleId(googleId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }
}
