package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.User;
import com.dhgroup.beta.service.BoardService;
import com.dhgroup.beta.web.dto.BoardPostDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BoardControllerUnitTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    public void 글작성() throws Exception{
        BoardPostDto boardPostDto = createBoardPostDto("글제목","글내용");
        User user = createUser("글쓴이");

        given(httpSession.getAttribute("nickName")).willReturn(user);
        given(boardService.write(any(BoardPostDto.class))).willReturn(1L);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(boardPostDto))
                        .content(new ObjectMapper().writeValueAsString(httpSession)))
                        .andExpect(status().isOk());

//        verify(boardService).write(boardPostDto);
    }

    @Test
     public void 좋아요() throws Exception{
        //given

        //when

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/board/like/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

        verify(boardService).likeIncrease(1L);
    }

    @Test
     public void 좋아요취소() throws Exception{
        //given

        //when

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/v1/board/likeRollback/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(boardService).likeRollback(1L);
    }

    private static User createUser(String nickName) {
        return User.builder().nickName(nickName).build();
    }

    private BoardPostDto createBoardPostDto(String title, String content) {
        return BoardPostDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}