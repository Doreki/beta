package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts,Long> {

//    List<Posts> findFirst10ByIdLessThanEqualOrderByIdDesc(@Param("id") Long id);

    @Query(value = "select p from Posts p join fetch p.member order by p.id desc",
            countQuery = "select count(p) from Posts p")
    Page<Posts> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "select p from Posts p join fetch p.member where p.id in :id")
    List<Posts> findLikedPosts(@Param("id")List<Long> likedPostsId);
    Posts findTopByOrderByIdDesc();
}
