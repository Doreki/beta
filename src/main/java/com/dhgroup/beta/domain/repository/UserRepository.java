package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Comment;
import com.dhgroup.beta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
