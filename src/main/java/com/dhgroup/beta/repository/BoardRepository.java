package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {

}
