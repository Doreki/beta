package com.dhgroup.beta.web;

import com.dhgroup.beta.repository.Board;
import com.dhgroup.beta.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerTest {
    private final static Long INITIAL_VALUE=10L;
    private final static Long START_ID=INITIAL_VALUE+1;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
    @AfterEach
    public void tearDown() throws Exception {
        boardRepository.deleteAll();
    }
    
    @Test
    public void 게시글목록() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/board/{lastIndex}";

        for(int i=1;i<=INITIAL_VALUE;i++) {
            boardWrite("글제목"+i,"글쓴이"+i,"글내용"+i);
        }

        int total = (int)boardRepository.count();
        //when
        mockMvc.perform(get(url,0L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(total)))
                .andExpect(jsonPath("$.limit",is(10)))
                .andExpect(jsonPath("$.boardResponseDtos[0].title",is("글제목10")))
                .andExpect(jsonPath("$.boardResponseDtos[9].title",is("글제목1")));
    }

    @Test
    public void 마지막게시글_테스트() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/board/{lastIndex}";

        //불러올 게시글이 없음
        mockMvc.perform(get(url,0L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg",is("LIST_ERR")));
    }



    @Test
    public void 게시글등록() throws Exception{
       //given
        String title = "글쓴이";
        String writer = "글쓴이";
        String content = "글내용";

        boardWrite(title,writer,content);

        //when
        List<Board> all = boardRepository.findAll();
        //then
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
    }

    public void boardWrite(String title,String writer, String content) throws Exception {
        String url = "http://localhost:" + port + "/api/v1/board";

        BoardPostDto postDto = BoardPostDto.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isOk());
    }
}