package com.dhgroup.beta.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table
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

    private Likes(Posts posts, Member member) {
        this.posts = posts;
        this.member = member;
    }

    public static Likes createLikes(Posts posts, Member member) {
        return Likes.builder().posts(posts).member(member).build();
    }
}
