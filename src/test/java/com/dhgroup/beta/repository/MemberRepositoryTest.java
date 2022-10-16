package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
     public void 카카오아이디로_멤버찾아오기() throws Exception{
        //given
        Member member = createMember("1","nickname", Provider.KAKAO);
        memberRepository.save(member);
        Member memberByGoogleId = memberRepository.findByAuthId(member.getAuthId(),member.getProvider()).get();
        //when

        //then
        assertThat(memberByGoogleId.getId()).isEqualTo(member.getId());
    }


    @Test
    public void 닉네임중복체크() throws Exception{
        //given
        Member member = createMember("1","nickname", Provider.KAKAO);
        memberRepository.save(member);
        //when
        boolean existsByNickname = memberRepository.existsByNickname(member.getNickname());
        //then
        assertThat(existsByNickname).isEqualTo(true);
    }

    @Test
     public void 닉네임_중복() throws Exception{
        //given
        Member member1 = createMember("1","nickname", Provider.KAKAO);
        Member member2 = createMember("2","nickname", Provider.KAKAO);
        memberRepository.save(member1);

        //then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member2));
    }


    private static Member createMember(String authId, String nickname, Provider provider) {
        return Member.builder().authId(authId).nickname(nickname).userTag("#0001").provider(provider).build();
    }
}

