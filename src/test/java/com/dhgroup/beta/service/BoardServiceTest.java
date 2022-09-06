package com.dhgroup.beta.service;

import com.dhgroup.beta.SpringConfig;
import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;

    Long id;
    @BeforeEach
    public void setUp() {
         id = boardService.write(BoardPostDto
                .builder()
                .title("글 제목")
                .content("글 내용")
                .writer("작성자").build());
    }
    @AfterEach
    public void cleanUp() {
        boardRepository.deleteAll();
    }
    @Test
    public void 글작성() {
        assertThat(boardRepository.findById(id).get().getId()).isEqualTo(id);
    }

    @Test
    public void 글_수정() {

        //when - 실행
                boardService.update(id, BoardUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build()); //repositroy 내용 수정
        //then - 검증
        assertThat(boardRepository.findById(id).get().getTitle()).isEqualTo("글제목 수정");
        assertThat(boardRepository.findById(id).get().getContent()).isEqualTo("글내용 수정");
        assertThat(boardRepository.findById(id).get().getWriter()).isEqualTo("작성자");
    }

    @Test
    public void 글수정_실패() {
        Long wrongId = id+1;
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> boardService.update(wrongId, BoardUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }


    @Test
    public void 글삭제() {
        //given
        String writer = "작성자"; //session으로 부터 얻어온 이름
        //when
        boardService.delete(id,writer);
        NoSuchElementException e = assertThrows(NoSuchElementException.class,() -> boardRepository.findById(id).get());
        //글을 찾을때 글이 없으면 삭제가 성공한 것
        //then
        assertThat(e.getMessage()).isEqualTo("No value present");
    }

    @Test
    public void 글삭제_실패() {
        //given - 상황
        String writer = "작성자 아님";
        //when - 실행
        boardService.delete(id,writer);
        //then - 검증, 글이 존재한다면 삭제 실패한 것
        Board board = boardRepository.findById(id).get();
        assertThat(board.getWriter()).isEqualTo("작성자");
    }

    @Test
    public void 글조회() {
        BoardResponseDto responseDto = boardService.read(id);
        assertThat(responseDto.getTitle()).isEqualTo("글 제목");
    }

    @Test
    public void 좋아요버튼() {
        //given
        boardService.likeIncrease(id);
        boardService.likeIncrease(id);
        //when
        Board board = boardRepository.findById(id).get();
        //then
        assertThat(board.getLikeCnt()).isEqualTo(2);
    }

    @Test
    public void 좋아요취소() {
        //given
        boardService.likeIncrease(id);
        boardService.likeIncrease(id);
        boardService.likeRollback(id);
        boardService.likeRollback(id);

        Board board = boardRepository.findById(id).get();
        //when
        //then
        assertThat(board.getLikeCnt()).isEqualTo(0);
    }

    @Test
    public void 좋아요취소실패() {
        boardService.likeRollback(id);

        Board board = boardRepository.findById(id).get();

        assertThat(board.getLikeCnt()).isEqualTo(0);
    }
}