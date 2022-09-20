package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
