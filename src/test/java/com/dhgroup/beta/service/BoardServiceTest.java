package com.dhgroup.beta.service;

import com.dhgroup.beta.SpringConfig;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

@SpringBootTest
public class BoardServiceTest {

    @Before
    public void cleanUp() {
        boardRepository.deleteAll();
    }

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;
//    @Autowired
//    BoardService boardService = new BoardService(boardRepository);

    @Test
    public void 글작성() {


        Long id = boardService.write(BoardPostDto
                .builder()
                .title("글 제목")
                .content("글 내용")
                .writer("작성자").build());
        Assertions.assertThat(boardRepository.findById(id).get().getId()).isEqualTo(id);
    }


}