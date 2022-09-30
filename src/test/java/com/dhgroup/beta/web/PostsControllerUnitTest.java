package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.dto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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

@WebMvcTest(PostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PostsControllerUnitTest {

    @MockBean
    private PostsService postsService;

    @MockBean
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void 글작성() throws Exception{
        PostsRequestDto postsRequestDto = createPostsPostDto("글제목","글내용");
        Member member = createMember("1","글쓴이");

        given(postsService.write(any(PostsRequestDto.class))).willReturn(1L);

         mockMvc.perform(
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

//    @Test
//     public void 글수정() throws Exception{
//        //given
//
//        PostsUpdateDto updateDto = PostsUpdateDto.builder().content("글내용").title("글제목").build();
////        given(postsService.update(1L,updateDto))
//        //when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.patch("/api/v1/posts/{id}", 1L)
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        //then
//        verify()
//    }

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