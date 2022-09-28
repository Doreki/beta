package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigCommentRepository extends JpaRepository<Reply,Long> {
}
