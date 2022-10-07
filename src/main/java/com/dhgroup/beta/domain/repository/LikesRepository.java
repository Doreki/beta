package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    void deleteByMemberIdAndPostsId(Long memberId, Long postsId);

    boolean existsByMemberIdAndPostsId(Long memberId, Long postsId);
}
