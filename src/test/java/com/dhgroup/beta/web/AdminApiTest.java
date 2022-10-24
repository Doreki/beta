package com.dhgroup.beta.web;

import com.dhgroup.beta.security.WithMockCustomMember;
import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Role;
import com.dhgroup.beta.security.PrincipalDetails;
import com.dhgroup.beta.service.PostsService;
import com.dhgroup.beta.web.controller.api.admin.AdminApi;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminApi.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AdminApiTest {

    @MockBean
    private PostsService postsService;


    @Autowired
    private MockMvc mockMvc;

    
    @Test
    @WithMockCustomMember(role = Role.ADMIN)
     public void 글삭제() throws Exception{
        //given

        Long postsId = 1L;
        //when
        mockMvc.perform(
                        delete("/api/v1/admin/{postsId}", postsId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        //then
        verify(postsService).deletePosts(postsId);
    }

    @Test
    @WithMockCustomMember(role = Role.MEMBER)
    public void 글삭제_실패() throws Exception{
        //given

        Long postsId = 1L;
        //when
        mockMvc.perform(
                        delete("/api/v1/admin/{postsId}", postsId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}