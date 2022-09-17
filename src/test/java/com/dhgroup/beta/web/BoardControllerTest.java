package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.repository.BoardRepository;
import com.dhgroup.beta.web.dto.BoardPostDto;
import com.dhgroup.beta.web.dto.BoardUpdateDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerTest {
    private final static Long INITIAL_VALUE=10L;
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
        int lastIndex = (int)(boardRepository.findTopByOrderByIdDesc().getId()-9);
        //when
        mockMvc.perform(get(url,0L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(total)))
                .andExpect(jsonPath("$.limit",is(10)))
                .andExpect(jsonPath("$.lastIndex",is(lastIndex)))
                .andExpect(jsonPath("$.posts[0].title",is("글제목10")))
                .andExpect(jsonPath("$.posts[9].title",is("글제목1")));
    }

    @Test
    public void 마지막게시글_테스트() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/board/{lastIndex}";

        //불러올 게시글이 없음
        mockMvc.perform(get(url,0L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
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
        Board findPost = all.get(0); //등록게시물
        //then
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getContent()).isEqualTo(content);
        assertThat(findPost.getWriter()).isEqualTo(writer);
    }

    @Test
    public void 게시물수정() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/board/{id}";

        String writer = "글쓴이";

        boardWrite("글제목",writer,"글내용");

        String updatedTitle = "수정제목";
        String updatedContent = "수정내용";

        BoardUpdateDto updateDto = BoardUpdateDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        Long id = findId(); //등록게시물

        //when
        mockMvc.perform(patch(url,id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        Long updatedId = findId(); //수정된 게시물

        Board findPost = boardRepository.findById(updatedId).get();
        //then
        assertThat(findPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(findPost.getContent()).isEqualTo(updatedContent);
        assertThat(findPost.getWriter()).isEqualTo(writer);
    }

    @Test
    public void 게시물없음() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/board/{id}";

        boardWrite("글제목","글쓴이","글내용");

        BoardUpdateDto updateDto = BoardUpdateDto.builder()
                .title("수정제목")
                .content("수정내용")
                .build();

        Long wrongId = findId()+1; //게시글이 삭제됐다고 가정
        //when
        mockMvc.perform(patch(url,wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.msg",is("POST_ERR")));
    }

    @Test
    public void 게시글삭제() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/board/{id}";

        boardWrite("글제목","글쓴이","글내용");

        Long id = findId();

        mockMvc.perform(delete(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(delete(url,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //한번더 삭제하려고 하면 예외발생
    }

    @Test
    public void 좋아요() throws Exception{
        String url = "http://localhost:" + port + "/api/v1/board/like/{id}";

        boardWrite("글제목","글쓴이","글내용");

        Long id = findId();

        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertThat(findPost().getLikeCnt()).isEqualTo(1);
    }

    @Test
    public void 좋아요취소() throws Exception{
        String url = "http://localhost:" + port + "/api/v1/board/like/{id}";

        boardWrite("글제목","글쓴이","글내용");

        Long id = findId();

        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

         url = "http://localhost:" + port + "/api/v1/board/likeRollback/{id}";
        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(findPost().getLikeCnt()).isEqualTo(0);
    }
    private void boardWrite(String title,String writer, String content) throws Exception {
        String url = "http://localhost:" + port + "/api/v1/board";

        BoardPostDto postDto = BoardPostDto.builder()
                .title(title)
                .content(content)
                .build();

        postDto.setWriter(writer);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                        .andExpect(status().isOk());
    }

    private Long findId() throws Exception {
        List<Board> all = boardRepository.findAll();
        Board findPost = all.get(0); //등록 게시물
        Long id = findPost.getId();

        return id;
    }

    private Board findPost() throws Exception {
        List<Board> all = boardRepository.findAll();
        Board findPost = all.get(0); //등록 게시물

        return findPost;
    }
}