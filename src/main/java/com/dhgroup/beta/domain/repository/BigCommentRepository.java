package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.BigComment;
import com.dhgroup.beta.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigCommentRepository extends JpaRepository<BigComment,Long> {
}
