package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.exception.NotFoundPostsException;
import com.dhgroup.beta.web.dto.LikesDto.LikesRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
@ExtendWith(SpringExtension.class)
@Import({PostsRepository.class, PostsService.class})
public class PostsServiceTest {

    @MockBean
    private PostsRepository postsRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private LikesRepository likesRepository;

    @Autowired
    PostsService postsService;


    @Test
     public void 글목록_불러오기() throws Exception{
        //given
        List<Posts> postsList = new ArrayList<>();
        Member member = createMember("글쓴이", "1", 1L);

        //게시글 생성
        for (int i = 0; i < 10; i++) {
            Posts posts = createPosts(member, "글제목", "글내용", 1L, true);
            postsList.add(posts);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Posts> page = new PageImpl<>(postsList);

        given(postsRepository.findAllByOrderByIdDesc(pageRequest)).willReturn(page);
        //when
        List<PostsResponseDto> postsResponseDtos = postsService.viewPosts(pageRequest);
        //then
        verify(postsRepository).findAllByOrderByIdDesc(any(PageRequest.class));
        assertThat(postsResponseDtos.size()).isEqualTo(10);
        assertThat(postsResponseDtos.get(0).getTitle()).isEqualTo("글제목");
        assertThat(postsResponseDtos.get(0).isLiked()).isEqualTo(false);
    }


    @Test
    public void 글목록_불러오기_실패() throws Exception{
        //given
        List<Posts> postsList = new ArrayList<>();
        Page<Posts> page = new PageImpl<>(postsList);
        PageRequest pageRequest = PageRequest.of(0, 10);

        //빈 리스트 반환
        given(postsRepository.findAllByOrderByIdDesc(pageRequest)).willReturn(page);
        //when

        NotFoundPostsException e = assertThrows(NotFoundPostsException.class, () -> postsService.viewPosts(pageRequest));
        //then
        assertThat(e.getMessage()).isEqualTo("더 이상 불러들일 게시글이 없습니다.");
    }
    @Test
    public void 좋아요여부() throws Exception{
        //given
        List<Posts> postsList = new ArrayList<>();
        Member memberByMobile = createMember("홍길동", "1", 1L);
        Member writer = createMember("글쓴이", "2", 2L);

        Posts likePosts = createPosts(writer, "글제목", "글내용", 1L, true);
        Posts noneLikePosts = createPosts(writer, "글제목", "글내용", 2L, true);

        postsList.add(likePosts);
        postsList.add(noneLikePosts);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Posts> page = new PageImpl<>(postsList);

        given(postsRepository.findAllByOrderByIdDesc(pageRequest)).willReturn(page);
        given(likesRepository.existsByMemberIdAndPostsId(memberByMobile.getId(), likePosts.getId())).willReturn(true);
        given(likesRepository.existsByMemberIdAndPostsId(memberByMobile.getId(), noneLikePosts.getId())).willReturn(false);

        //when
        List<PostsResponseDto> postsResponseDtos = postsService.viewPosts(memberByMobile.getId(), pageRequest);
        PostsResponseDto likePostsDto = postsResponseDtos.get(0);
        PostsResponseDto noneLikePostsDto = postsResponseDtos.get(1);

        //then
        verify(postsRepository).findAllByOrderByIdDesc(any(PageRequest.class));
        assertThat(likePostsDto.isLiked()).isEqualTo(true);
        assertThat(noneLikePostsDto.isLiked()).isEqualTo(false);
    }

    @Test
     public void 작성자체크() throws Exception{
        //given
        Long postsId = 1L;
        Long memberId = 1L;

        Member member = createMember("글쓴이","1", 1L);
        Posts posts = createPosts(member,"글제목","글내용", 1L, true);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));//멤버
        given(postsRepository.findById(postsId)).willReturn(Optional.of(posts)); //포스트에 저장된멤버
        //when
        boolean value = postsService.isWriter(postsId, memberId);
        //then
        verify(postsRepository).findById(postsId);
        verify(memberRepository).findById(memberId);

        assertThat(value).isEqualTo(true);
    }

    @Test
    public void 작성자체크실패() throws Exception{
        //given
        Long postsId = 1L;
        Long memberId = 1L;
        Long wrongMemberId = 2L;

        Member wrongMember = createMember("홍길동","1", wrongMemberId);
        Member member = createMember("글쓴이","2", memberId);
        Posts posts = createPosts(member,"글제목","글내용", 1L, true);


        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.of(wrongMember));//다른멤버
        given(postsRepository.findById(postsId)).willReturn(Optional.of(posts)); //포스트에 저장된멤버
        //when
        boolean value = postsService.isWriter(postsId, wrongMemberId);
        //then
        verify(postsRepository).findById(postsId);
        verify(memberRepository).findById(wrongMemberId);

        assertThat(value).isEqualTo(false);
    }

    @Test
     public void 글작성() throws Exception{
        //given
        Member member = createMember("글쓴이","1", 1L);
        Posts posts = createPosts(member,"글제목","글내용", 1L, true);
        PostsRequestDto requestDto = createPostsRequestDto(1L, "글제목", "글내용");

        given(memberRepository.findById(requestDto.getMemberId())).willReturn(Optional.of(member));
        given(postsRepository.save(any(Posts.class))).willReturn(posts);
        //when
        Long postsId = postsService.writePosts(requestDto); //requestDto가 Posts 엔티티로 변환되는지 확인
        //then
        verify(postsRepository).save(any(Posts.class));
        verify(memberRepository).findById(requestDto.getMemberId());
    }

    @Test
     public void 좋아요() throws Exception{
        //given
        Member member = createMember("글쓴이", "1", 1L);
        Posts posts = createPosts(member, "글제목", "글내용", 1L, true);
        LikesRequestDto likesRequestDto = createLikesRequestDto(member.getId(), posts.getId());

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(postsRepository.findById(posts.getId())).willReturn(Optional.of(posts));

        //when
        postsService.likeIncrease(likesRequestDto);
        //then
        verify(likesRepository).save(any(Likes.class));
        assertThat(posts.getLikeCount()).isEqualTo(1);
    }

    @Test
    public void 좋아요_취소() throws Exception{
        //given
        Member member = createMember("글쓴이", "1", 1L);
        Posts posts = createPosts(member, "글제목", "글내용", 1L, true);
        LikesRequestDto likesRequestDto = createLikesRequestDto(member.getId(),posts.getId());

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(postsRepository.findById(posts.getId())).willReturn(Optional.of(posts));

        //when
        postsService.likeIncrease(likesRequestDto);
        postsService.likeRollback(likesRequestDto);
        //then
        verify(likesRepository).deleteByMemberIdAndPostsId(member.getId(),posts.getId());
        assertThat(posts.getLikeCount()).isEqualTo(0);
    }

    @Test
     public void 좋아요_게시물_찾아오기() throws Exception{
        //given
        Member member = createMember("글쓴이", "1", 1L);

        List<Posts> postsList = new ArrayList();
        List<Likes> likesList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Posts posts = createPosts(member, "글제목", "글내용", 1L, true);
            postsList.add(posts);
            Likes likes = Likes.createLikes(posts, member);
            likesList.add(likes);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Likes> page = new PageImpl<>(likesList);

        given(likesRepository.findLikesByMemberIdOrderByDesc(member.getId(),pageRequest)).willReturn(page);
        given(postsRepository.findLikedPostsByLatestOrder(any(List.class))).willReturn(postsList);
        given(likesRepository.findByPostsIdAndMemberId(anyLong(),anyLong())).willReturn(Optional.of(likesList.get(0)));
        //when
        List<PostsResponseDto> postsResponseDtos = postsService.viewLikedPosts(member.getId(), pageRequest);
        //then
        verify(likesRepository).findLikesByMemberIdOrderByDesc(member.getId(),pageRequest);
        verify(postsRepository).findLikedPostsByLatestOrder(any(List.class));
        assertThat(postsResponseDtos.size()).isEqualTo(10);
    }
    
    @Test
     public void 좋아요_여부_업데이트() throws Exception{
        //given
        Member member = createMember("홍길동", "1", 1L);
        List<Posts> postsList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Posts posts = createPosts(member, "글제목", "글내용", i + 0L, false);
            postsList.add(posts);
        }
        given(likesRepository.existsByMemberIdAndPostsId(eq(member.getId()), anyLong())).willReturn(true);
        //when
        postsService.updateWhetherIsLiked(member.getId(), postsList);
        //then
        assertThat(postsList.get(0).isLiked()).isEqualTo(true);
    }

    @Test
     public void 좋아요_시간_업데이트() throws Exception{
        //given
        Member member = createMember("홍길동", "1", 1L);
        List<Posts> postsList = new ArrayList<>();

        Posts posts = createPosts(member, "글제목", "글내용", 1L, false);
        postsList.add(posts);

        Likes likes = Likes.createLikes(posts, member);

        given(likesRepository.findByPostsIdAndMemberId(eq(member.getId()), anyLong())).willReturn(Optional.of(likes));
        //when
        postsService.updateLikedDate(member.getId(),postsList);
        //then
        assertThat(postsList.get(0).getLikedDate()).isEqualTo(likes.getLikedDate());
    }


    private static PostsRequestDto createPostsRequestDto(Long memberId, String title, String content) {
        return PostsRequestDto.builder().memberId(memberId).title(title).content(content).build();
    }

    private static LikesRequestDto createLikesRequestDto(Long memberId, Long postsId) {
        return LikesRequestDto.builder().postsId(postsId).memberId(memberId).build();
    }

    private static Posts createPosts(Member member, String title, String content, Long postsId, boolean isLiked) {
        return Posts.builder().id(postsId).title(title).content(content).member(member).likeCount(0).isLiked(isLiked).build();
    }

    private static Member createMember(String nickName, String googleId, Long memberId) {
        return Member.builder().id(memberId).nickname(nickName).googleId(googleId).build();
    }
}