package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LikesRepositoryTest {

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostsRepository postsRepository;

    @Test
     public void 좋아요_삭제() throws Exception{
        //given
        Likes likes = createLikes();
        likesRepository.save(likes);
        //when
        Long memberId = likes.getMember().getId();
        Long postsId = likes.getPosts().getId();
        likesRepository.deleteByMemberIdAndPostsId(memberId, postsId);
        //then
        List<Likes> allLikes = likesRepository.findAll();
        assertThat(allLikes.size()).isEqualTo(0); //db에서 삭제됐으면 like 수가 0
    }

    private Likes createLikes() {
        Member member = Member.builder().googleId("1").nickname("홍길동").build();
        memberRepository.save(member);
        Posts posts = Posts.builder().title("글제목").content("글내용").member(member).build();
        postsRepository.save(posts);
        return Likes.createLikes(posts,member);
    }


}
