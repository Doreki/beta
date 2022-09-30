package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsControllerTest {
    private final static Long INITIAL_VALUE=10L;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

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
        postsRepository.deleteAll();
    }
    
    @Test
    public void 게시글목록() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/posts/{lastIndex}";

        for(int i=1;i<=INITIAL_VALUE;i++) {
            postsWrite("글제목"+i,"글쓴이"+i,"글내용"+i);
        }

        int total = (int) postsRepository.count();
        int lastIndex = (int)(postsRepository.findTopByOrderByIdDesc().getId()-9);
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
        String url = "http://localhost:" + port + "/api/v1/posts/{lastIndex}";

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

        postsWrite(title,writer,content);

        //when
        List<Posts> all = postsRepository.findAll();
        Posts findPost = all.get(0); //등록게시물
        //then
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getContent()).isEqualTo(content);
        assertThat(findPost.getMember().getNickname()).isEqualTo(writer);
    }

    @Test
    public void 게시물수정() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/posts/{id}";

        String writer = "글쓴이";

        postsWrite("글제목",writer,"글내용");

        String updatedTitle = "수정제목";
        String updatedContent = "수정내용";

        PostsUpdateDto postsUpdateDto = PostsUpdateDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        Long id = findId(); //등록게시물

        //when
        mockMvc.perform(patch(url,id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postsUpdateDto)))
                .andExpect(status().isOk());

        Long updatedId = findId(); //수정된 게시물

        Posts findPost = postsRepository.findById(updatedId).get();
        //then
        assertThat(findPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(findPost.getContent()).isEqualTo(updatedContent);
        assertThat(findPost.getMember().getNickname()).isEqualTo(writer);
    }

    @Test
    public void 게시물없음() throws Exception {
        //given
        String url = "http://localhost:" + port + "/api/v1/posts/{id}";

        postsWrite("글제목","글쓴이","글내용");

        PostsUpdateDto updateDto = PostsUpdateDto.builder()
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
        String url = "http://localhost:" + port + "/api/v1/posts/{id}";

        postsWrite("글제목","글쓴이","글내용");

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
        String url = "http://localhost:" + port + "/api/v1/posts/like/{id}";

        postsWrite("글제목","글쓴이","글내용");

        Long id = findId();

        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertThat(findPost().getLikeCnt()).isEqualTo(1);
    }

    @Test
    public void 좋아요취소() throws Exception{
        String url = "http://localhost:" + port + "/api/v1/posts/like/{id}";

        postsWrite("글제목","글쓴이","글내용");

        Long id = findId();

        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

         url = "http://localhost:" + port + "/api/v1/posts/likeRollback/{id}";
        mockMvc.perform(patch(url,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(findPost().getLikeCnt()).isEqualTo(0);
    }
    private void postsWrite(String title,String writer, String content) throws Exception {
        String url = "http://localhost:" + port + "/api/v1/posts";

        Member member = Member.builder().nickname(writer).googleId("1").build();
        PostsRequestDto postsRequestDto = PostsRequestDto.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postsRequestDto)))
                        .andExpect(status().isOk());
    }

    private Long findId() throws Exception {
        List<Posts> all = postsRepository.findAll();
        Posts findPost = all.get(0); //등록 게시물
        Long id = findPost.getId();

        return id;
    }

    private Posts findPost() throws Exception {
        List<Posts> all = postsRepository.findAll();
        Posts findPost = all.get(0); //등록 게시물

        return findPost;
    }
}