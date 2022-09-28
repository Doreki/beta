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

    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(MemberRequestDto memberRequestDto) {

        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        member = createUserTag(member);
        return member.getId();
    }

    @Transactional
    public Member singIn(String googleId) {
    }

    public static Member createUserTag(Member member) {
        Long userId = member.getId();
        String userTag = "";
        if(userId/10 == 0) {
            userTag = "000"+userId;
        } else if (userId/100 == 0) {
            userTag = "00" + userId;
        } else if (userId/1000 == 0) {
            userTag = "0" +userId;
        }
        String nickname = member.getNickname()+"#"+userTag;
        member.updateNickname(nickname);

        return member;
    }

}
