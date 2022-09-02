package com.dhgroup.beta.service;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class BoardService {

    private final BoardRepository boardRepository;


//    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    @Transactional
    public Long write(BoardPostDto boardPostDto) {
            return boardRepository.save(boardPostDto.toEntity()).getId(); //반환값 BoardRepository
    }

    @Transactional
    public void update(BoardUpdateDto boardUpdateDto) {

            boardRepository.
    }
}
