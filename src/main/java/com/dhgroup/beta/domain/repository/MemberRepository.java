package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

//    boolean existsByGoogleId(String googleId);
}
