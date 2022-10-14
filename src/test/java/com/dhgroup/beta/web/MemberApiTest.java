package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.controller.api.MemberApi;
import com.dhgroup.beta.web.dto.MemberDto.JoinRequestDto;
import com.dhgroup.beta.web.dto.MemberDto.MemberResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberApi.class)
@MockBean(JpaMetamodelMappingContext.class) //Auditing 기능을 위해 mock객체로 올려둠
public class MemberApiTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
     public void 회원가입() throws Exception{
        //given
        String url = "/api/v1/member/1h2g2yysh297h2s";
        JoinRequestDto joinRequestDto = createMemberRequestDto("1","홍길동");
        //when
        given(memberService.isDuplicated(joinRequestDto.getGoogleId())).willReturn(false);
        given(memberService.join(any(JoinRequestDto.class))).willReturn(1L);
        //then
        mockMvc.perform(
                        post(url)
                                .content(new ObjectMapper().writeValueAsString(joinRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status",is(1)))
                                .andDo(print());

        verify(memberService).join(any(JoinRequestDto.class));
    }
    @Test
    public void 회원가입_실패() throws Exception{
        //given
        String url = "/api/v1/member/1h2g2yysh297h2s";
        JoinRequestDto joinRequestDto = createMemberRequestDto("1","홍길동");

        given(memberService.isDuplicated(joinRequestDto.getGoogleId())).willReturn(true);
        given(memberService.join(any(JoinRequestDto.class))).willReturn(1L);
        //then
        mockMvc.perform(
                        post(url)
                                .content(new ObjectMapper().writeValueAsString(joinRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status",is(-1)))
                                .andExpect(jsonPath("$.msg",is("DUPLICATED_ID")))
                                .andDo(print());
    }
    @Test
     public void 로그인() throws Exception{
        //given
        Member member = createMember("1","글쓴이");
        String url = "/api/v1/member/login/{googleId}";
        String googleId = member.getGoogleId();
        MemberResponseDto memberResponseDto = MemberResponseDto.createMemberResponseDto(member);


        given(memberService.logIn(googleId)).willReturn(memberResponseDto);
        //when
                    mockMvc.perform(
                                get(url,googleId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status",is(1)))
                                .andExpect(jsonPath("$.data.nickname",is("글쓴이")))
                                .andDo(print());
        //then
        verify(memberService).logIn(googleId);
    }


    @Test
     public void 닉네임변경() throws Exception{
        //given
        String url = "/api/v1/member/{memberId}";
        JoinRequestDto joinRequestDto = createMemberRequestDto("1", "홍길동");
        //when
        mockMvc.perform(
                        patch(url,1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(joinRequestDto)))
                                .andExpect(status().isOk())
                                .andDo(print());
        //then
        verify(memberService).updateNickname(1L, joinRequestDto.getNickname());
    }

    private static Member createMember(String googleId, String nickname) {
        return Member.builder().googleId(googleId).nickname(nickname).build();
    }

    private static JoinRequestDto createMemberRequestDto(String googleId, String nickname) {
        return JoinRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }
}
