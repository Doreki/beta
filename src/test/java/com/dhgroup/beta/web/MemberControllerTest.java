package com.dhgroup.beta.web;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.service.MemberService;
import com.dhgroup.beta.web.dto.MemberRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class) //Auditing 기능을 위해 mock객체로 올려둠
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
     public void 로그인성공() throws Exception{
        //given
        Member member = createMember("1","글쓴이");
        String url = "/api/v1/member/login";
        String googleId = member.getGoogleId();

        given(memberService.isValidMember(googleId)).willReturn(true);
        given(memberService.logIn(googleId)).willReturn(member);
        //when
                    mockMvc.perform(
                                post(url)
                                .content(googleId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.googleId",is("1")))
                                .andExpect(jsonPath("$.nickname",is("글쓴이")))
                                .andDo(print());
        //then
        verify(memberService).logIn(googleId);
    }

    @Test
     public void 로그인실패() throws Exception{
        //given
        Member member = createMember("1","글쓴이");
        String url = "/api/v1/member/login";
        String googleId = member.getGoogleId();
        given(memberService.isValidMember(member.getGoogleId())).willReturn(false);
        //when
        ResultActions resultActions = mockMvc.perform(
                                 post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(googleId))
                                .andExpect(status().isSeeOther())
                                .andExpect(jsonPath("$.msg",is("REDIRECT_TO_SIGNUP")))
                                .andDo(print());
        //then
    }

    private static Member createMember(String googleId, String nickname) {
        return Member.builder().googleId(googleId).nickname(nickname).build();
    }

    private static MemberRequestDto createMemberRequestDto(String googleId,String nickname) {
        return MemberRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }
}
