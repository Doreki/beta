package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberRequestDto;
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

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
     public void 회원가입() throws Exception{
        MemberRequestDto memberRequestDto = createMemberRequestDto("1","글쓴이");
        //given
        String url = "/api/v1/user/1h2g2yysh297h2s";
        //when
        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk());
        //then
        verify(memberService).signUp(any(MemberRequestDto.class));
    }

//    @Test
//     public void 로그인() throws Exception{
//        //given
//        Member member = createMember();
//        String url = "api/b1/user";
//        given(memberService.signIn).willReturn(true);
//        //when
//        ResultActions resultActions = mockMvc.perform(
//                        MockMvcRequestBuilders.post(url)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(new ObjectMapper().writeValueAsString(member)))
//                .andExpect(status().isOk());
//        //then
//        verify(memberService).signIn();
//    }

    private static Member createMember() {
        return Member.builder().googleId("1").nickname("글쓴이").build();
    }

    private static MemberRequestDto createMemberRequestDto(String googleId,String nickname) {
        return MemberRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }
}
