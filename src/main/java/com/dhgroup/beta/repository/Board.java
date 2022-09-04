package com.dhgroup.beta.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Entity
public class Board {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto_increment 설정
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private Integer viewCnt;
    private Integer commentCnt;

    /*
    id는 자동생성,viewCnt,commentCnt는 메서드로 값 주입예정;
     */
    @Builder
    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    //updateDto에서 받은 내용으로 글을 수정해줌
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
