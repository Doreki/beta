package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotFoundGoogleIdException;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@Import({MemberRepository.class, MemberService.class})
@ExtendWith(SpringExtension.class)
public class MemberServiceUnitTest {

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;


    @Test
     public void 로그인_실패() throws Exception{
        //given
        Member member = createMember("1", "글쓴이");
        given(memberRepository.findByGoogleId(anyString())).willReturn(Optional.of(member));
        //when
        ;
        //then
        NotFoundGoogleIdException e = assertThrows(NotFoundGoogleIdException.class, () -> memberService.logIn(null));
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 회원입니다.");
    }

    private static Member createMember(String googleId, String nickname) {
        return Member.builder()
                .googleId(googleId)
                .nickname(nickname)
                .build();
    }
}
