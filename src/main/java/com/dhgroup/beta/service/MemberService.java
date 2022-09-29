package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

//    @Transactional
//    public Member singIn(String googleId) {
//
//        return new Member();
//    }

    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(MemberRequestDto memberRequestDto) {

        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        String nickname = member.getNickname()+member.createUserTag();
        member.updateNickname(nickname);
        return member.getId();
    }

    public boolean memberCheck(String googleId) {
        return memberRepository.existsByGoogleId(googleId);
    }

    public Member logIn(String googleId) {
        return memberRepository.findByGoogleId(googleId);
    }
}
