package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.web.dto.MemberCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long signUp(MemberCreateDto memberCreateDto) {

        Member member = memberCreateDto.toEntity();
        memberRepository.save(member);
        member = createUserTag(member);
        return member.getId();
    }

    private static Member createUserTag(Member member) {
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
