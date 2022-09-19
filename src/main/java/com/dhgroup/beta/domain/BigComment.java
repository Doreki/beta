package com.dhgroup.beta.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
public class BigComment {

    @Id
    @GeneratedValue
    @Column(name = "big_comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, name = "big_comment_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
