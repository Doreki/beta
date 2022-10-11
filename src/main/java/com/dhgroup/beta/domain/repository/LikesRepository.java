package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    public void deleteByMemberIdAndPostsId(Long memberId, Long postsId);


    public boolean existsByMemberIdAndPostsId(Long memberId, Long postsId);

    @Query("select l from Likes l join fetch l.posts where exist " +
            "(select p from Posts p where l.member.id = :memberId and l.postsId in :postsIds )")
    public List<Boolean> existsByMemberIdAndPostsId(@Param("memberId") Long memberId, @Param("postsIds") List<Long> postsId);

    @Query(value = "select l from Likes l join fetch l.posts "+
            "where l.member.id = :id order by l.id Desc",
            countQuery = "select count(l) from Likes l")
    public Page<Likes> findLikesByMemberIdOrderByDesc(@Param("id") Long memberId, Pageable pageable);

    public Optional<Likes> findByPostsIdAndMemberId(Long postsId,Long memberId);
}
