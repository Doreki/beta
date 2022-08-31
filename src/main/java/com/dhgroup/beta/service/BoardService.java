package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.BoardDto;
import com.dhgroup.beta.repository.BoardRepository;

public class BoardService {
    private BoardDto boardDto;
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardDto write(BoardDto boardDto) {
        BoardDto boardDto1 =boardRepository.save(boardDto);
        return boardDto1;
    }
}
