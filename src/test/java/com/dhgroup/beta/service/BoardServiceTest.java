package com.dhgroup.beta.service;

import com.dhgroup.beta.SpringConfig;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;

    @After
    public void cleanUp() {
        boardRepository.deleteAll();
    }
    @Test
    public void 글작성() {
        Long id = boardService.write(BoardPostDto
                .builder()
                .title("글 제목")
                .content("글 내용")
                .writer("작성자").build());
        assertThat(boardRepository.findById(id).get().getId()).isEqualTo(id);
    }

    @Test
    public void 글_수정() {
        //given - 준비
        Long id = boardService.write(BoardPostDto
                .builder()
                .title("글 제목")
                .content("글 내용")
                .writer("작성자").build()); //repository에 내용 저장

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
        Long id = boardService.write(BoardPostDto
                .builder()
                .title("글 제목")
                .content("글 내용")
                .writer("작성자").build());

        Long wrongId = id+1;
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> boardService.update(wrongId, BoardUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다. no="+wrongId);
    }



}