package com.dhgroup.beta.repository;

import com.dhgroup.beta.web.dto.BoardResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<BoardResponseDto> findFirst3ByIdLessThanEqualOrderByIdDesc(Long id);
    Board findTopByOrderByIdDesc();
}
