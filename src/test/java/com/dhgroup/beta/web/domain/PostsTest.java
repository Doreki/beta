package com.dhgroup.beta.web.domain;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.PostsStatus;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostsTest {

    @Test
     public void 게시글_업데이트_상태() throws Exception{
        //given
        Posts posts = createPosts();
        //when
        posts.update("수정제목", "수정내용");
        //then
        assertThat(posts.getStatus()).isEqualTo(PostsStatus.MODIFIED);
    }


    @Test
     public void 게시글_생성_상태() throws Exception{
        //given
        Member member = createMember();
        PostsRequestDto postsRequestDto = createPostsRequestDto();
        //when
        Posts posts = postsRequestDto.toEntity(member);
        //then
        assertThat(posts.getStatus()).isEqualTo(PostsStatus.CREATED);
    }

    private static PostsRequestDto createPostsRequestDto() {
        return PostsRequestDto.builder().title("제목").content("글내용").build();
    }

    private static Member createMember() {
        return Member.builder().googleId("1").nickname("홍길동").build();
    }
    private static Posts createPosts() {
        return Posts.builder().title("글제목").content("글내용").build();
    }
}
