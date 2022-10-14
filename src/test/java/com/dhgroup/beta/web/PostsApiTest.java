package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.controller.api.PostsApi;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsApi.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PostsApiTest {

    @MockBean
    private PostsService postsService;

    @MockBean
    private PostsRepository postsRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private LikesRepository likesRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void 글작성() throws Exception{
        PostsRequestDto postsRequestDto = createPostsReqeustDto("글제목","글내용");

        given(postsService.writePosts(any(PostsRequestDto.class))).willReturn(1L);

         mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postsRequestDto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.status",is(1)))
                        .andDo(print());

        verify(postsService).writePosts(any(PostsRequestDto.class));
    }


    @Test
     public void 글수정() throws Exception{
        //given

        PostsUpdateDto updateDto = PostsUpdateDto.builder().memberId(1L).content("글내용").title("글제목").build();
        Long postsId = 1L;
        Long memberId = updateDto.getMemberId();

        given(postsService.isWriter(postsId,memberId)).willReturn(true);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/posts/{postsId}", postsId)
                                .content(new ObjectMapper().writeValueAsString(updateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status",is(1)))
                                .andDo(print());
        //then
        verify(postsService).isWriter(postsId,memberId);
        verify(postsService).updatePosts(eq(postsId),any(PostsUpdateDto.class));
    }

    @Test
    public void 글수정_실패() throws Exception{
        //given

        PostsUpdateDto updateDto = PostsUpdateDto.builder().memberId(1L).content("글내용").title("글제목").build();
        Long postsId = 1L;
        Long memberId = updateDto.getMemberId();

        given(postsService.isWriter(postsId,memberId)).willReturn(false);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/posts/{postsId}", postsId)
                                .content(new ObjectMapper().writeValueAsString(updateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isForbidden());
        //then
    }

    @Test
    public void 글목록_불러오기_로그인안함() throws Exception{
        //given

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<PostsResponseDto> postsResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
        postsResponseDtos.add(createPostsResponseDto("글제목"+i, "글내용"+i));
        }


        given(postsService.viewPosts(any(PageRequest.class))).willReturn(postsResponseDtos);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/posts/list/{memberId}",0L)
                                .content(new ObjectMapper().writeValueAsString(pageRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.size()",is(5)))
                                .andDo(print());
        //then
        verify(postsService).viewPosts(any(PageRequest.class));
    }

    @Test
    public void 글목록_불러오기_로그인함() throws Exception{
        //given

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<PostsResponseDto> postsResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            postsResponseDtos.add(createPostsResponseDto("글제목"+i, "글내용"+i));
        }


        given(postsService.viewPosts(eq(1L),any(PageRequest.class))).willReturn(postsResponseDtos);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/posts/list/{memberId}",1L)
                                .content(new ObjectMapper().writeValueAsString(pageRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()",is(5)))
                .andDo(print());
        //then
        verify(postsService).viewPosts(eq(1L),any(PageRequest.class));
    }
    
    @Test
     public void 글삭제() throws Exception{
        //given
        Long postsId = 1L;
        Long memberId = 1L;
        given(postsService.isWriter(postsId,memberId)).willReturn(true);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/posts/{postsId}", postsId)
                                .content(new ObjectMapper().writeValueAsString(memberId))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        //then
        verify(postsService).deletePosts(postsId);
    }

    @Test
     public void 좋아요() throws Exception{
        //given
        LikesRequestDto likesRequestDto = createLikesRequestDto(1L, 1L);
        
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/posts/like/")
                                .content(new ObjectMapper().writeValueAsString(likesRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        //then
        verify(postsService).likeIncrease(any(LikesRequestDto.class));
    }

    @Test
     public void 좋아요취소() throws Exception{
        //given
        LikesRequestDto likesRequestDto = createLikesRequestDto(1L, 1L);
        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/posts/like/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(likesRequestDto)))
                                .andExpect(status().isOk());
        //then
        verify(postsService).likeRollback(any(LikesRequestDto.class));
    }


    private static LikesRequestDto createLikesRequestDto(Long memberId, Long postsId) {
        return LikesRequestDto.builder().memberId(memberId).postsId(postsId).build();
    }

    private PostsRequestDto createPostsReqeustDto(String title, String content) {
        return PostsRequestDto.builder()
                .title(title)
                .content(content)
                .build();
    }

    private PostsResponseDto createPostsResponseDto(String title, String content) {
        return PostsResponseDto.builder()
                .title(title)
                .content(content)
                .build();
    }

}