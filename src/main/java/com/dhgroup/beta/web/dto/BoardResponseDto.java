package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.repository.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardResponseDto {
    private String title;
    private String content;
    private String writer;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdDate;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.likeCnt = board.getLikeCnt();
        this.commentCnt = board.getCommentCnt();
        this.createdDate = board.getCreatedDate();
    }
}
