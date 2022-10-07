package com.dhgroup.beta.repository;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.repository.LikesRepository;
import com.dhgroup.beta.domain.repository.MemberRepository;
import com.dhgroup.beta.domain.repository.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
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
        Member member = createMember("1", "홍길동");
        Posts posts = createPosts(member, "글제목", "글내용");
        Likes likes = Likes.createLikes(posts, member);
        likesRepository.save(likes);
        //when
        Long memberId = likes.getMember().getId();
        Long postsId = likes.getPosts().getId();
        likesRepository.deleteByMemberIdAndPostsId(memberId, postsId);
        //then
        List<Likes> allLikes = likesRepository.findAll();
        assertThat(allLikes.size()).isEqualTo(0); //db에서 삭제됐으면 like 수가 0
    }

    @Test
     public void 좋아요_여부() throws Exception{
        //given
        Member member = createMember("1", "홍길동");
        Posts posts = createPosts(member, "글제목", "글내용");
        Likes likes = Likes.createLikes(posts, member);

        likesRepository.save(likes);
        //when
        boolean isExisted = likesRepository.existsByMemberIdAndPostsId(member.getId(),posts.getId());
        //then
        assertThat(isExisted).isEqualTo(true);
    }


    private Posts createPosts(Member member, String title, String content) {
        Posts posts = Posts.builder().title(title).content(content).member(member).build();
        postsRepository.save(posts);
        return posts;
    }

    private Member createMember(String googleId, String nickname) {
        Member member = Member.builder().googleId(googleId).nickname(nickname).build();
        memberRepository.save(member);
        return member;
    }


}
