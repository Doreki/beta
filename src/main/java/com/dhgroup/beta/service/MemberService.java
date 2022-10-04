package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.OverlapMemberException;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.web.dto.MemberRequestDto;
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

    public Member logIn(String googleId) {
        return memberRepository.findByGoogleId(googleId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
        member.updateNickname(nickname);
    }

    @Transactional
    public void isDuplicated(String googleId) {
        if(memberRepository.existsByGoogleId(googleId))
            throw new OverlapMemberException("이미 존재하는 회원입니다.");
    }
}
