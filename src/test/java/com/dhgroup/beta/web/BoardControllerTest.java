package com.dhgroup.beta.web;

import com.dhgroup.beta.Exception.NotFoundBoardException;
import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardResponseDto;
import org.junit.After;
import org.junit.Before;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String url = "http://localhost:" + port + "/api/v1/board";

        for(int i=1;i<=10;i++) {
            boardWrite("글제목"+i,"글쓴이"+i,"글내용"+i);
        }

        ResponseEntity<Map> responseEntity = testRestTemplate.getForEntity(url+"/{lastIndex}",Map.class, 0);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map pageHandler = responseEntity.getBody(); //response 바디에 담긴 내용을 객체로 받아온다.
        assertThat(pageHandler.get("boardResponseDtos").getClass()).isEqualTo(ArrayList.class);
        assertThat(pageHandler.get("total")).isEqualTo((int)(boardRepository.count())); //타입불일치로 테스트실패해서 형변환
    }

    @Test
    public void 마지막게시글_테스트() {
        String url = "http://localhost:" + port + "/api/v1/board";

//        for(int i=1;i<=10;i++) {
//            boardWrite("글제목"+i,"글쓴이"+i,"글내용"+i);
//        }

        ResponseEntity<Map> responseEntity = testRestTemplate.getForEntity(url+"/{lastIndex}",Map.class, 1L);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.)
        Map exceptions = responseEntity.getBody();
        exceptions.forEach((key, value)
                -> System.out.println("key: " + key + ", value: " + value));
        assertThat(exceptions.size()).isEqualTo(1);
        assertThat(exceptions.get("msg")).isEqualTo("LIST_ERR");
    }


    @Test
    public void 게시글등록() {
//        //given
        String title = "글쓴이";
        String writer = "글쓴이";
        String content = "글내용";

        ResponseEntity<Long> responseEntity = boardWrite(title,writer,content);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //첫번째 등록된 게시물 가져옴
        //id값으로 찾으면 AutoIncrement로 인해 DB의 영향을 받기 때문에 리스트로 가져와야함
        List<Board> all = boardRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
    }

    public ResponseEntity<Long> boardWrite(String title,String writer, String content) {

        String url = "http://localhost:" + port + "/api/v1/board";

        BoardPostDto postDto = BoardPostDto.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();

        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, postDto, Long.class);

        return responseEntity;
    }
}