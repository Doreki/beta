package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    public void deleteByMemberIdAndPostsId(Long memberId, Long postsId);


    public boolean existsByMemberIdAndPostsId(Long memberId, Long postsId);

    @Query("select count(l) > 0 from Likes l" +
            " where l.member.id = :memberId and" +
            " l.posts.id in :postsIds")
    public List<Boolean> existsByMemberIdAndPostsId(@Param("memberId") Long memberId, @Param("postsIds") List<Long> postsIds);

    @Query(value = "select l from Likes l" +
            " join fetch l.posts"+
            " where l.member.id = :id order by l.id Desc",
            countQuery = "select count(l) from Likes l")
    public Page<Likes> findLikesByMemberIdOrderByDesc(@Param("id") Long memberId, Pageable pageable);

    @Query("select l from Likes l" +
            " join fetch l.posts" +
            " where l.member.id = :memberId" +
            " and l.posts.id in :postsIds")
    public List<Likes> findLikesByMemberIdAndPostsIds(@Param("memberId") Long memberId, @Param("postsIds")List<Long> postsIds);


}
