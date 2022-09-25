package com.dhgroup.beta.domain;

import com.dhgroup.beta.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, name = "comment_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "comment")
    private List<BigComment> bigCommentList = new ArrayList<>();

    @Builder
    public Comment(String content, Board board, Member member) {
        this.content = content;
        this.member = member;
        setBoard(board);
    }

    //연관관계 편의 메서드
    public void setBoard(Board board) {
        if(this.board != null) {
            this.board.getCommentList().remove(this);
        }
        this.board = board;
        board.getCommentList().add(this); //board 객체가 생성될때 member 객체에 주입
    }

    public void addBigComment(BigComment bigComment) {
        this.bigCommentList.add(bigComment);
        if(bigComment.getComment() !=this) {
            bigComment.setComment(this);
        }
    }
}
