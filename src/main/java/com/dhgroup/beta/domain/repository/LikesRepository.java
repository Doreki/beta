package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    void deleteByMemberIdAndPostsId(Long memberId, Long postsId);
    boolean existsByMemberIdAndPostsId(Long memberId, Long postsId);
    @Query(value = "select l from Likes l join fetch l.posts"+
            " where l.member.id = :id order by l.id Desc",
            countQuery = "select count(l) from Likes l")
    Page<Likes> findLikesByMemberIdOrderByDesc(@Param("id") Long memberId, Pageable pageable);
}
