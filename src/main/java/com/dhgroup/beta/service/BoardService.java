package com.dhgroup.beta.service;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


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
    public Long update(Long id, BoardUpdateDto boardUpdateDto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //시간 차로 인해 게시글이 없을 수도 있기 때문에 예외 던져줌
        //DB에서 해당 게시글을 찾아오고 없으면 예외 던짐
        board.update(boardUpdateDto.getTitle(),boardUpdateDto.getContent());
        //게시글이 있으면 글 내용을 수정
        return id;
    }


    @Transactional
    public Long delete(Long id,String writer) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        if(board.getWriter() == writer) //게시글 작성자와 session의 작성자가 똑같다면
        boardRepository.delete(board);
        return id;
    }

    @Transactional
    public BoardResponseDto read(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //Entity의 내용을 Dto에 담는다
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .viewCnt(board.getViewCnt())
                .commentCnt(board.getCommentCnt())
                .build();
    }
}
