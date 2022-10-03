//package com.dhgroup.beta.service;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.BDDMockito.*;
//
//import com.dhgroup.beta.domain.Posts;
//import com.dhgroup.beta.domain.Member;
//import com.dhgroup.beta.domain.repository.MemberRepository;
//import com.dhgroup.beta.domain.repository.PostsRepository;
//import com.dhgroup.beta.web.dto.PostsResponseDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//
//@ExtendWith(SpringExtension.class)
//@Import({PostsRepository.class, PostsService.class})
//public class PostsServiceUnitTest {
//
//    @MockBean
//    private PostsRepository postsRepository;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @Autowired
//    PostsService postsService;
//
//
//    @Test
//     public void 글목록_불러오기() throws Exception{
//        //given
//
//        Long startId = 5L;
//        List<Posts> postsList = new ArrayList<>();
//        Member member = createMember("글쓴이", "1");
//
//        //게시글 생성
//        for (int i = 0; i < 5; i++) {
//            Posts posts = createPosts(member, "글제목", "글내용");
//            postsList.add(posts);
//        }
//        given(postsRepository.findFirst10ByIdLessThanEqualOrderByIdDesc(startId)).willReturn(postsList);
//        //when
//
//        List<PostsResponseDto> postsResponseDtos = postsService.viewList(startId);
//        //then
//        verify(postsRepository).findFirst10ByIdLessThanEqualOrderByIdDesc(startId);
//        assertThat(postsResponseDtos.size()).isEqualTo(5);
//    }
//
//    @Test
//     public void 작성자체크() throws Exception{
//        //given
//        Long postsId = 1L;
//        String googleId = "1";
//
//        Member member = createMember("글쓴이",googleId);
//        Posts posts = createPosts(member,"글제목","글내용");
//
//        given(memberRepository.findByGoogleId(googleId)).willReturn(Optional.of(member));//멤버
//        given(postsRepository.findById(postsId)).willReturn(Optional.of(posts)); //포스트에 저장된멤버
//        //when
//        boolean value = postsService.authorCheck(postsId, googleId);
//        //then
//        verify(postsRepository).findById(postsId);
//        verify(memberRepository).findByGoogleId(googleId);
//
//        assertThat(value).isEqualTo(true);
//    }
//
//    @Test
//    public void 작성자체크실패() throws Exception{
//        //given
//        Long postsId = 1L;
//        String googleId = "1";
//        String wrongGoogleId = "2";
//
//        Member wrongMember = createMember("홍길동",wrongGoogleId);
//
//        Member member = createMember("글쓴이",googleId);
//        Posts posts = createPosts(member,"글제목","글내용");
//
//
//        given(memberRepository.findByGoogleId(wrongGoogleId)).willReturn(Optional.of(wrongMember));//다른멤버
//        given(postsRepository.findById(postsId)).willReturn(Optional.of(posts)); //포스트에 저장된멤버
//        //when
//        boolean value = postsService.authorCheck(postsId, wrongGoogleId);
//        //then
//        verify(postsRepository).findById(postsId);
//        verify(memberRepository).findByGoogleId(wrongGoogleId);
//
//        assertThat(value).isEqualTo(false);
//    }
//
//    private static Posts createPosts(Member member, String title, String content) {
//        return Posts.builder().title(title).content(content).member(member).build();
//    }
//
//    private static Member createMember(String nickName, String googleId) {
//        return Member.builder().nickname(nickName).googleId(googleId).build();
//    }
//}