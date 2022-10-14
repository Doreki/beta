package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotExistMemberException;
import com.dhgroup.beta.web.dto.MemberDto.JoinRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@Import({MemberRepository.class, MemberService.class})
@ExtendWith(SpringExtension.class)
public class MemberServiceTest {

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;


    @Test
     public void 로그인_실패() throws Exception{
        //given
        Member member = createMember(1L,"1", "글쓴이");
        given(memberRepository.findByGoogleId(anyString())).willReturn(Optional.of(member));
        //when

        //then
        NotExistMemberException e = assertThrows(NotExistMemberException.class, () -> memberService.logIn(null));
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 회원입니다.");
    }

    @Test
     public void 회원가입() throws Exception{
        Member member = createMember(1L,"1", "글쓴이");
        JoinRequestDto requestDto = createRequestDto("1", "글쓴이");
        //given
        given(memberRepository.save(any(Member.class))).willReturn(member);
        //when
        memberService.join(requestDto);
        //then
        verify(memberRepository).save(any(Member.class));
        assertThat(member.getNickname()).isEqualTo("글쓴이#0001");
    }

    private static JoinRequestDto createRequestDto(String googleId, String nickname) {
        return JoinRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }

    private static Member createMember(Long id,String googleId, String nickname) {
        return Member.builder()
                .id(id)
                .googleId(googleId)
                .nickname(nickname)
                .build();
    }
}
