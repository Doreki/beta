package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class BoardResponseDto {
    private Board board;
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdDate;


//    public void setBoard(Board board) {
//        this.board = board;
//    }

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.likeCnt = board.getLikeCnt();
        this.commentCnt = board.getCommentCnt();
        this.createdDate = board.getCreatedDate();
    }
    public BoardResponseDto(Long id, String title, String content, String writer, Integer likeCnt, Integer commentCnt, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.createdDate = createdDate;
    }
}
