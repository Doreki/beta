package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
     public void 회원가입() throws Exception{
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("1","글쓴이");
        Long memberId = memberService.signUp(memberRequestDto);
        //when
        Member member = memberRepository.findById(memberId).get(); //db에 저장된 닉네임
        String userTag = member.createUserTag(); //userTag메서드가 만들어준 닉네임

        //then
        assertThat(member.getNickname()).isEqualTo("글쓴이"+userTag);
    }

    @Test
    public void 회원수정() throws Exception{
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("1","글쓴이");
        Long memberId = memberService.signUp(memberRequestDto);
        //when
        Member member = memberRepository.findById(memberId).get(); //db에 저장된 닉네임
        String userTag = member.createUserTag(); //userTag메서드가 만들어준 닉네임
        member.updateNickname("홍길동");

        //then
        assertThat(member.getNickname()).isEqualTo("홍길동"+userTag);
    }

    private static MemberRequestDto createMemberRequestDto(String googleId, String nickname) {
        return MemberRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }

    private static Member createMember(String googleId, String nickname) {
        return Member.builder().googleId(googleId).nickname(nickname).build();
    }
}
