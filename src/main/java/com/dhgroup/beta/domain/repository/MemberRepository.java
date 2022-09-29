package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsByGoogleId(@Param("google_id") String googleId);

    Optional<Member> findByGoogleId(@Param("google_id") String googleId);
}
