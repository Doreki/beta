package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts,Long> {

    List<Posts> findFirst10ByIdLessThanEqualOrderByIdDesc(@Param("posts_id") Long id);
    Posts findTopByOrderByIdDesc();
}
