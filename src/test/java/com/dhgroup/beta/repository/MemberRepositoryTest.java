package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.KakaoMember;
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
        KakaoMember kakaoMember = createKakaoMember("1","nickname", Provider.KAKAO);
        memberRepository.save(kakaoMember);
        Member memberByAuthId = memberRepository.findByAuthId(kakaoMember.getAuthId(),kakaoMember.getProvider()).get();
        //when

        //then
        assertThat(memberByAuthId.getId()).isEqualTo(kakaoMember.getId());
    }


    @Test
    public void 닉네임중복체크() throws Exception{
        //given
        KakaoMember kakaoMember = createKakaoMember("1","nickname", Provider.KAKAO);
        memberRepository.save(kakaoMember);
        //when
        boolean existsByNickname = memberRepository.existsByNickname(kakaoMember.getNickname());
        //then
        assertThat(existsByNickname).isEqualTo(true);
    }

    @Test
     public void 닉네임_중복() throws Exception{
        //given
        KakaoMember kakaoMember1 = createKakaoMember("1","nickname", Provider.KAKAO);
        KakaoMember kakaoMember2 = createKakaoMember("2","nickname", Provider.KAKAO);
        memberRepository.save(kakaoMember1);

        //then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(kakaoMember2));
    }

    @Test
     public void 아이디로_유저찾기() throws Exception{
        //given
        BasicMember member = createBasicMember("id","1234","nickname",Provider.BASIC);
        memberRepository.save(member);
        //when
        BasicMember findMember = memberRepository.findByMemberName(member.getUsername()).get();
        //then
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }


    private static KakaoMember createKakaoMember(String authId, String nickname, Provider provider) {
        return KakaoMember.builder().authId(authId).nickname(nickname).userTag("#0001").provider(provider).build();
    }

    private static BasicMember createBasicMember(String username,String password, String nickname, Provider provider) {
        return BasicMember.builder().username(username).password(password).nickname(nickname).userTag("#0001").provider(provider).build();
    }
}

