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

@NoArgsConstructor(access = AccessLevel.PROTECTED) //불필요한 생성자 접근을 막기 위함
@Getter
@Entity
public class Board extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto_increment 설정
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false, name="board_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer likeCnt;
    private Integer commentCnt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    /*
    id는 자동생성,likeCnt,commentCnt는 메서드로 값 주입예정;
     */
    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        likeCnt=0;
        commentCnt=0;
    }

    //updateDto에서 받은 내용으로 글을 수정해줌
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void likeIncrease() {
        ++this.likeCnt;
    }

    public void likeCancle() {
        if(likeCnt>0)
           this.likeCnt = likeCnt-1;
    }

}
