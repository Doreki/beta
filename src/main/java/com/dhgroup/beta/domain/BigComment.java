package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BigComment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "big_comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, name = "big_comment_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BigComment(String content, Comment comment, Member member) {
        this.content = content;
        this.member = member;
        setComment(comment);
    }

    public void setComment(Comment comment) {
        if(this.comment != null) {
            this.comment.getBigCommentList().remove(this);
        }
        this.comment = comment;
        comment.getBigCommentList().add(this);
    }
}
