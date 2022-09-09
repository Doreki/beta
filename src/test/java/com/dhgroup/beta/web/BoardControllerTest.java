package com.dhgroup.beta.web;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BoardRepository boardRepository;


    @After
    public void tearDown() throws Exception {
        boardRepository.deleteAll();
    }


    @Test
    public void 게시글목록() {
    }

    @Test
    public void 게시글등록() {
        //given
        String title = "글쓴이";
        String writer = "글쓴이";
        String content = "글내용";
        BoardPostDto postDto = BoardPostDto.builder()
                        .title(title)
                        .writer(writer)
                        .content(content)
                        .build();

        String url = "http://localhost:" + port + "/board";

        //when
        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, postDto, Long.class); //Url에 맵핑된 메서드에 포스트형식으로 보낸다


        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //첫번째 등록된 게시물 가져옴
        //id값으로 찾으면 AutoIncrement로 인해 DB의 영향을 받기 때문에 리스트로 가져와야함
        List<Board> all = boardRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
    }
}