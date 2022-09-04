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
    public void update(Long id, BoardUpdateDto boardUpdateDto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. no="+id));
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        board.update(boardUpdateDto.getTitle(),boardUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
    }
}
