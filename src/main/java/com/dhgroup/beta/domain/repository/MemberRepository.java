package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsByNickname(String nickname);
    @Query("select m from Member m where m.authId =:authId and m.provider = com.dhgroup.beta.domain.member.MemberProvider.KAKAO")
    Optional<Member> findByKakaoId(@Param("authId") String authId);
}
