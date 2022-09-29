package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
     public void 구글아이디로_멤버찾아오기() throws Exception{
        //given
        Member member = createMember("1","nickname");
        memberRepository.save(member);
        Member memberByGoogleId = memberRepository.findByGoogleId(member.getGoogleId()).get();
        //when

        //then
        assertThat(memberByGoogleId.getId()).isEqualTo(member.getId());
    }

    @Test
     public void 구글아이디로_멤버존재여부조회() throws Exception{
        //given
        Member member = createMember("1","nickname");
        memberRepository.save(member);
        //when
        boolean existsByGoogleId = memberRepository.existsByGoogleId(member.getGoogleId());
        //then
        assertThat(existsByGoogleId).isEqualTo(true);
    }


    private static Member createMember(String googleId,String nickname) {
        return Member.builder().googleId(googleId).nickname(nickname).build();
    }
}
