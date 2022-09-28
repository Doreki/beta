package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.web.dto.MemberRequestDto;
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
public class MemberServiceUnitTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
     public void 회원가입() throws Exception{
        Member member = createMember("1", "글쓴이");
        //given
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(member.createUserTag()).willReturn("#0001");

        //when
        memberService.signUp(MemberRequestDto.builder().googleId("1").nickname("글쓴이").build());

        //then
        verify(memberRepository).save(any(Member.class));
    }

    private static Member createMember(String googleId, String nickname) {
        return Member.builder()
                .googleId(googleId)
                .nickname(nickname)
                .build();
    }
}
