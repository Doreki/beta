package com.dhgroup.beta.web.domain;

import com.dhgroup.beta.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {

    @Test
     public void 유저태그_생성() throws Exception{
        //given
        Member member1 = createMember(1L, "홍길동", "1");
        Member member2 = createMember(10L, "홍길동", "1");
        Member member3 = createMember(100L, "홍길동", "1");
        Member member4 = createMember(1000L, "홍길동", "1");
        Member member5 = createMember(10000L, "홍길동", "1");

        String userTag1 = member1.createUserTag();
        String userTag2 = member2.createUserTag();
        String userTag3 = member3.createUserTag();
        String userTag4 = member4.createUserTag();
        String userTag5 = member5.createUserTag();
        //when
        assertThat(userTag1).isEqualTo("#0001");
        assertThat(userTag2).isEqualTo("#0010");
        assertThat(userTag3).isEqualTo("#0100");
        assertThat(userTag4).isEqualTo("#1000");
        assertThat(userTag5).isEqualTo("#10000");
        //then
    }

    private static Member createMember(Long id, String nickname, String googleId) {
        return Member.builder().id(id).nickname(nickname).googleId(googleId).build();
    }
}
