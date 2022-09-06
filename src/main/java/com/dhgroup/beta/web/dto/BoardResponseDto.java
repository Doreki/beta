package com.dhgroup.beta.web.dto;

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

    @Builder
    public BoardResponseDto(String title, String content, String writer, Integer likeCnt, Integer commentCnt, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.createdDate = createdDate;
    }
}
