package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.ExistNicknameException;
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
        String nickname = member.getNickname()+ member.createUserTag();
        member.updateNickname(nickname);
        return member.getId();
    }

    public Member logIn(String googleId) {
        return memberRepository.findByGoogleId(googleId).orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
    }

    public void updateNickname(Long id, String googleId) {
        Member member = memberRepository.findById(id).get();
        member.updateNickname(googleId);
    }

    public void isDuplicated(String nickname) {
        if(memberRepository.existsByNickname(nickname))
            throw new ExistNicknameException("이미 존재하는 닉네임입니다.");
    }
}
