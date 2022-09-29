package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostsControllerUnitTest {

    @Mock
    private PostsService postsService;

    @InjectMocks
    private PostsController postsController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postsController).build();
    }

    @Test
    public void 글작성() throws Exception{
        PostsRequestDto postsRequestDto = createPostsPostDto("글제목","글내용");
        Member member = createMember("1","글쓴이");

        given(postsService.write(any(PostsRequestDto.class))).willReturn(1L);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postsRequestDto)))
                        .andExpect(status().isOk());

    }

    @Test
     public void 좋아요() throws Exception{
        //given

        //when

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/posts/like/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

        verify(postsService).likeIncrease(1L);
    }

    @Test
     public void 좋아요취소() throws Exception{
        //given

        //when

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/posts/likeRollback/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postsService).likeRollback(1L);
    }

    private static Member createMember(String googleId,String nickName) {
        return Member.builder().googleId(googleId).nickname(nickName).build();
    }

    private PostsRequestDto createPostsPostDto(String title, String content) {
        return PostsRequestDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}