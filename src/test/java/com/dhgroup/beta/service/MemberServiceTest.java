package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.web.dto.MemberCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
     public void 회원가입() throws Exception{
        Member member = createMember();
        //given
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        given(MemberService.createUserTag(any(Member.class))).willReturn(member);
        memberService.signUp(new MemberCreateDto("1", "글쓴이"));

        //then
        verify(memberRepository).save(any(Member.class));
    }

    private static Member createMember() {
        return Member.builder()
                .googleId("1")
                .nickName("글쓴이")
                .build();
    }
}
