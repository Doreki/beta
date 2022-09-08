package com.dhgroup.beta.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findFirst3ByIdLessThanEqualOrderByIdDesc(Long id);
    Board findTopByOrderByIdDesc();
}
