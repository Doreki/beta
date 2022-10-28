package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsByNickname(String nickname);
    @Query("select m from Member m where m.authId =:authId and m.provider = :provider")
    Optional<Member> findByAuthId(@Param("authId") String authId, @Param("provider") Provider provider);

    Optional<BasicMember> findByUsername(String username);
}
