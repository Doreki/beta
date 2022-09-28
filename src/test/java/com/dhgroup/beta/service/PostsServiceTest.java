package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.exception.NotFoundPostsException;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.web.dto.PostsResponseDto;
import com.dhgroup.beta.web.dto.PostsUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class PostsServiceTest {

    private final static Integer LIMIT=10; //
    private final static Long START_ID=11L;
    private final static Long LAST_VALUE=START_ID-LIMIT+1;
    @Autowired
    PostsRepository postsRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostsService postsService;

    @Test
    public void 글작성() {
        Long id= postsWrite("글제목","글내용");
        assertThat(postsRepository.findById(id).get().getId()).isEqualTo(id);
    }

    @Test
    public void 글_수정() {
        Long id= postsWrite("글제목","글내용");

                PostsUpdateDto postsUpdateDto = PostsUpdateDto
                        .builder()
                        .title("글제목 수정")
                        .content("글내용 수정")
                        .build();
        //when - 실행
                postsService.update(id, postsUpdateDto); //repositroy 내용 수정
            Posts posts = postsRepository.findById(id).get(); //DB에서 수정된 객체를 가져옴
        //then - 검증
        assertThat(posts.getTitle()).isEqualTo("글제목 수정");
        assertThat(posts.getContent()).isEqualTo("글내용 수정");
        assertThat(posts.getMember().getNickname()).isEqualTo("글쓴이");

    }

    @Test
     public void 글수정_시간() throws Exception{
        //given
        Long id= postsWrite("글제목","글내용");
        Posts posts = postsRepository.findById(id).get();
        LocalDateTime createdDate = posts.getCreatedDate(); //게시글 날짜

        //when
        PostsUpdateDto postsUpdateDto = PostsUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build();
        //then
        postsService.update(id, postsUpdateDto); //repositroy 내용 수정
        postsRepository.flush();

        LocalDateTime modifiedDate = posts.getModifiedDate();

        assertThat(modifiedDate).isAfter(createdDate); //글 수정 후에 수정시간이 바꼈는지 확인
    }

    @Test
    public void 글수정_실패() {
        Long id= postsWrite("글제목","글내용");
        Long wrongId = id+1;
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.update(wrongId, PostsUpdateDto
                .builder()
                .title("글제목 수정")
                .content("글내용 수정")
                .build()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }


    @Test
    public void 글삭제() {
        Long id= postsWrite("글제목","글내용");
        //given
        String nickName = "글쓴이"; //session으로 부터 얻어온 이름
        //when
        postsService.delete(id);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,() -> postsService.findById(id));
        //글을 찾을때 글이 없으면 삭제가 성공한 것
        //then
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

//    @Test
//    public void 글삭제_실패() {
//        Long id= postsWrite("글제목","글내용");
//        //given - 상황
//        Long wrongId = id+1;
//        String nickName = "작성자 아님";
//        //when - 실행
//        postsService.delete(wrongId);
//        //then - 검증, 글이 존재한다면 삭제 실패한 것
//        Posts posts = postsRepository.findById(id).get();
//        assertThat(posts.getMember().getNickname()).isEqualTo("글쓴이");
//    }

    @Test
    public void 글조회() {
        Long id= postsWrite("글제목","글내용");
        LocalDateTime now = LocalDateTime.now();
        PostsResponseDto responseDto = postsService.read(id);
        assertThat(responseDto.getTitle()).isEqualTo("글제목");
        assertThat(responseDto.getCreatedDate()).isBefore(now); //Dto에 생성시간 잘 들어갔는지 확인,생성이 먼저이기 때문에 생성 시간이 지금보다 더 전이어야함
    }

    @Test
    public void 좋아요버튼() {
        Long id= postsWrite("글제목","글내용");
        //given
        postsService.likeIncrease(id);
        postsService.likeIncrease(id);
        //when
        Posts posts = postsRepository.findById(id).get();
        //then
        assertThat(posts.getLikeCnt()).isEqualTo(2);
    }

    @Test
    public void 좋아요취소() {
        Long id= postsWrite("글제목","글내용");
        //given
        postsService.likeIncrease(id);
        postsService.likeIncrease(id);
        postsService.likeRollback(id);
        postsService.likeRollback(id);

        Posts posts = postsRepository.findById(id).get();
        //when
        //then
        assertThat(posts.getLikeCnt()).isEqualTo(0);
    }

    @Test
    public void 좋아요취소실패() {
        Long id= postsWrite("글제목","글내용");
        postsService.likeRollback(id);

        Posts posts = postsRepository.findById(id).get();

        assertThat(posts.getLikeCnt()).isEqualTo(0);
    }

    @Test
    public void 글목록불러오기() {
        //given 총 글 11개추가
        for(int i=1;i<=11;i++) {
            Member member = Member.builder().googleId(""+i).nickname("글쓴이").build();
            memberRepository.save(member);

        postsWrite("글제목"+i,"글내용"+i,member);
        }

        //when
        List<PostsResponseDto> postsList = postsService.viewList(START_ID);
        PostsResponseDto firstPosts = postsList.get(0);
        PostsResponseDto lastPosts = postsList.get(9);
        //then
        assertThat(postsList.size()).isEqualTo(LIMIT);
        assertThat(firstPosts.getTitle()).isEqualTo("글제목"+START_ID);
        assertThat(lastPosts.getTitle()).isEqualTo("글제목"+(START_ID-9));

        //then
        postsList = postsService.viewList(lastPosts.getId()-1);
        assertThat(postsList.size()).isEqualTo(START_ID-10);
        assertThat(postsList.get(0).getTitle()).isEqualTo("글제목"+(START_ID-10));
    }

    @Test
    public void 글목록불러오기_마지막_페이지() {
        Member member = Member.builder().googleId("1").nickname("글쓴이").build();
        memberRepository.save(member);

        //given
        //게시글이 아무 것도 없을때
        System.out.println("postsRepository.findTopByOrderByIdDesc() = " + postsRepository.findTopByOrderByIdDesc());
        NotFoundPostsException e = assertThrows(NotFoundPostsException.class,() -> postsService.viewList(START_ID));
        assertThat(e.getMessage()).isEqualTo("더 이상 불러들일 게시글이 없습니다.");

        for(int i=1;i<=START_ID;i++) {
            postsWrite("글제목"+i,"글내용"+i,member);
        }

        //when 마지막 게시글을 불러올때
        e = assertThrows(NotFoundPostsException.class,() -> postsService.viewList(1L));

        //then
        assertThat(e.getMessage()).isEqualTo("더 이상 불러들일 게시글이 없습니다.");
    }

    public Long postsWrite(String title, String content) {
        Member member = Member.builder().nickname("글쓴이").build();

        Posts posts = Posts
                .builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

        return postsRepository.save(posts).getId();
    }

    public Posts postsWrite(String title, String content, Member member) {
        Posts posts = Posts
                .builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
//        posts.setMember(member);

        return postsRepository.save(posts);
    }
}