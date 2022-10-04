package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void save() {
        //given

        String title = "테스트 게시글";
        String content = "테스트 본문";
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {
//        Member member = createMember();

        LocalDateTime now = LocalDateTime.of(2022,9,6,0,0,0);
             postsRepository.save(Posts.builder()
                        .title("글 제목")
                        .content("글 내용")
                        .build());

             List<Posts> postsList = postsRepository.findAll();

             Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>> createDate=" +
                posts.getCreatedDate() +", modifiedDate="+
                posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

    @Test
     public void 최신글찾기() throws Exception{
        //given
        Member member = createMember("홍길동", "1");
            memberRepository.save(member);

        for (int i = 1; i <= 10; i++) {
        Posts posts = createPosts(member, "글제목"+i, "글내용");
            postsRepository.save(posts);
        }
        //when
        Posts findRecentPosts = postsRepository.findTopByOrderByIdDesc();
        //then
        assertThat(findRecentPosts.getTitle()).isEqualTo("글제목10");
    }


    @Test
    public void 글목록_불러오기_10개() throws Exception{
        //given
        for (int i = 1; i <= 100; i++) {
            Member member = createMember("홍길동"+i, "1");
            memberRepository.save(member);

            Posts posts = createPosts(member, "글제목"+i, "글내용");
            postsRepository.save(posts);
        }

        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<Posts> PostsList = postsRepository.findAllByOrderByIdDesc(pageable);
        //then
        assertThat(PostsList.getTotalPages()).isEqualTo(10);
        assertThat(PostsList.getNumberOfElements()).isEqualTo(10);
        assertThat(PostsList.getContent().get(0).getTitle()).isEqualTo("글제목100");
        assertThat(PostsList.getContent().get(9).getTitle()).isEqualTo("글제목91");
    }

    @Test
    public void 글목록_불러오기_10개이하() throws Exception{
        //given
        for (int i = 1; i <= 3; i++) {
            Member member = createMember("홍길동"+i, "1");
            memberRepository.save(member);

            Posts posts = createPosts(member, "글제목"+i, "글내용");
            postsRepository.save(posts);
        }

        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<Posts> postsList = postsRepository.findAllByOrderByIdDesc(pageable);

        //then
        assertThat(postsList.getTotalPages()).isEqualTo(1);
        assertThat(postsList.getNumberOfElements()).isEqualTo(3);
        assertThat(postsList.getContent().get(0).getTitle()).isEqualTo("글제목3");
        assertThat(postsList.getContent().get(2).getTitle()).isEqualTo("글제목1");
    }

    private static Posts createPosts(Member member, String title, String content) {
        return Posts.builder().title(title).content(content).member(member).build();
    }

    private static Member createMember(String nickName, String googleId) {
        return Member.builder().nickname(nickName).googleId(googleId).build();
    }
}