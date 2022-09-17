package com.dhgroup.beta.domain.repository;

import com.dhgroup.beta.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findFirst10ByIdLessThanEqualOrderByIdDesc(Long id);
    Board findTopByOrderByIdDesc();
}
