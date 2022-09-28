package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.repository.PostsRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    MemberRepository memberRepository;

//    @Before
//    public void cleanup() {
//        postsRepository.deleteAll();
//    }

    @Test
    public void save() {
        //given
        Member member = createMember();

        String title = "테스트 게시글";
        String content = "테스트 본문";
        String writer = "테스트 작가";
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .member(member).build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getMember().getNickname()).isEqualTo(writer);
    }

    @Test
    public void BaseTimeEntity_등록() {
        Member member = createMember();

        LocalDateTime now = LocalDateTime.of(2022,9,6,0,0,0);
             postsRepository.save(Posts.builder()
                        .title("글 제목")
                        .content("글 내용")
                        .member(member)
                        .build());

             List<Posts> postsList = postsRepository.findAll();

             Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>> createDate=" +
                posts.getCreatedDate() +", modifiedDate="+
                posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

    private Member createMember() {
        Member member = Member.builder().googleId("1").nickName("테스트 작가").build();
        memberRepository.save(member);
        return member;
    }
}