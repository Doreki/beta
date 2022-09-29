package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;


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
}