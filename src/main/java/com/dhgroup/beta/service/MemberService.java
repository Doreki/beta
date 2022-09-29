package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotFoundGoogleIdException;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        memberRepository.save(member);
        String nickname = member.getNickname()+member.createUserTag();
        member.updateNickname(nickname);
        return member.getId();
    }

    public boolean isValidMember(String googleId) {
        return memberRepository.existsByGoogleId(googleId);
    }

    public Member logIn(String googleId) {
        return memberRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundGoogleIdException("존재하지 않는 회원입니다."));
    }
}
