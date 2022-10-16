package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.controller.api.MemberApi;
import com.dhgroup.beta.web.dto.MemberDto.KakaoJoinRequestDto;
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
     public void 카카오_회원가입() throws Exception{
        //given
        String url = "/api/v1/member/kakao/1h2g2yysh297h2s";
        KakaoJoinRequestDto kakaoJoinRequestDto = createMemberRequestDto("1","홍길동");
        //when
        given(memberService.isDuplicated(kakaoJoinRequestDto.getAuthId(), Provider.KAKAO)).willReturn(false);
        given(memberService.join(any(KakaoJoinRequestDto.class))).willReturn(1L);
        //then
        mockMvc.perform(
                        post(url)
                                .content(new ObjectMapper().writeValueAsString(kakaoJoinRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status",is(1)))
                                .andDo(print());

        verify(memberService).join(any(KakaoJoinRequestDto.class));
    }
    @Test
    public void 카카오_회원가입_실패() throws Exception{
        //given
        String url = "/api/v1/member/kakao/1h2g2yysh297h2s";
        KakaoJoinRequestDto kakaoJoinRequestDto = createMemberRequestDto("1","홍길동");

        given(memberService.isDuplicated(kakaoJoinRequestDto.getAuthId(),Provider.KAKAO)).willReturn(true);
        given(memberService.join(any(KakaoJoinRequestDto.class))).willReturn(1L);
        //then
        mockMvc.perform(
                        post(url)
                                .content(new ObjectMapper().writeValueAsString(kakaoJoinRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status",is(-1)))
                                .andExpect(jsonPath("$.msg",is("DUPLICATED_ID")))
                                .andDo(print());
    }
    @Test
     public void 카카오_로그인() throws Exception{
        //given
        Member member = createMember("1","글쓴이");
        String url = "/api/v1/member/kakao/login/{authId}";
        String authId = member.getAuthId();
        MemberResponseDto memberResponseDto = MemberResponseDto.createMemberResponseDto(member);


        given(memberService.login(authId,Provider.KAKAO)).willReturn(memberResponseDto);
        //when
                    mockMvc.perform(
                                get(url,authId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status",is(1)))
                                .andExpect(jsonPath("$.data.nickname",is("글쓴이")))
                                .andDo(print());
        //then
        verify(memberService).login(authId,Provider.KAKAO);
    }


    @Test
     public void 닉네임변경() throws Exception{
        //given
        String url = "/api/v1/member/{memberId}";
        KakaoJoinRequestDto kakaoJoinRequestDto = createMemberRequestDto("1", "홍길동");
        Member member = createMember("1","홍길동");
        given(memberService.findMemberByMemberId(1L)).willReturn(member);
        //when
        mockMvc.perform(
                        patch(url,1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(kakaoJoinRequestDto)))
                                .andExpect(status().isOk())
                                .andDo(print());
        //then
        verify(memberService).updateNickname(1L, kakaoJoinRequestDto.getNickname());
    }

    private static Member createMember(String authId, String nickname) {
        return Member.builder().authId(authId).nickname(nickname).build();
    }

    private static KakaoJoinRequestDto createMemberRequestDto(String googleId, String nickname) {
        return KakaoJoinRequestDto.builder().authId(googleId).nickname(nickname).build();
    }
}
