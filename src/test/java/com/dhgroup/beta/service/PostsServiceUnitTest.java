package com.dhgroup.beta.service;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@ExtendWith(SpringExtension.class)
@Import({PostsRepository.class, PostsService.class})
public class PostsServiceUnitTest {

    @MockBean
    private PostsRepository postsRepository;


    @Autowired
    PostsService postsService;

//    @Test
//    public void 글작성() {
//
//        Member member = Member.builder().nickName("글쓴이").build();
//        given().willReturn(member);
//        Posts posts = Posts.builder()
//                .title("글제목")
//                .content("글내용")
//                .member()
//                .build();
//
//        given(PostsRepository.save(postDto.toEntity())).willReturn(1L);
//
//        Long id = PostsService.write(postsPostDto);
//
//        assertThat(id).isEqualTo(1);
//    }
//
//    @Test
//    public void 게시글목록() {
//        List<Posts> PostsList = postsList();
//        Long id = postsList.get(9).getId();
//        doReturn(postsList()).when(PostsRepository).findFirst10ByIdLessThanEqualOrderByIdDesc(id);
//    }
//    private PostsDto postsDto() {
//        return PostsDto.builder()
//                .title("글제목")
//                .content("글내용")
//                .build();
//    }
//
//    private List<Posts> postsList() {
//        List<Posts> postsList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            postsList.add(new Posts("title","content","writer"));
//        }
//        return postsList;
//    }

    @Test
     public void 글목록_불러오기() throws Exception{
        //given

        Long startId = 5L;
        List<Posts> postsList = new ArrayList<>();
        Member member = createMember("글쓴이", "1");

        //게시글 생성
        for (int i = 0; i < 5; i++) {
            Posts posts = createPosts(member, "글제목", "글내용");
            postsList.add(posts);
        }
        given(postsRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)).willReturn(postsList);
        //when

        List<PostsResponseDto> postsResponseDtos = postsService.viewList(startId);
        //then
        verify(postsRepository).findFirst10ByIdLessThanEqualOrderByIdDesc(startId);
        assertThat(postsResponseDtos.size()).isEqualTo(5);
    }


    private static Posts createPosts(Member member, String title, String content) {
        return Posts.builder().title(title).content(content).member(member).build();
    }

    private static Member createMember(String nickName, String googleId) {
        return Member.builder().nickname(nickName).googleId(googleId).build();
    }
}