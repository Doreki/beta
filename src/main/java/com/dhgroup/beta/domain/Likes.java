package com.dhgroup.beta.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
