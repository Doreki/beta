package com.dhgroup.beta.domain;

import com.dhgroup.beta.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "likes_unique",
                columnNames = {"posts_id", "member_id"})})
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private LocalDateTime likedDate;

    private Likes(Posts posts, Member member) {
        this.posts = posts;
        this.member = member;
        this.likedDate = LocalDateTime.now();
        //연관관계 편의 메서드
        posts.getLikesList().add(this);
    }

    /*
   테스트를 위해 시간 생성분리
     */
    public static Likes createLikes(Posts posts, Member member) {
        return Likes.builder().posts(posts).member(member).likedDate(LocalDateTime.now()).build();
    }
}
