package com.dhgroup.beta.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardResponseDto {
    private String title;
    private String content;
    private String writer;
    private Integer likeCnt;
    private Integer commentCnt;

    @Builder

    public BoardResponseDto(String title, String content, String writer, Integer likeCnt, Integer commentCnt) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
    }
}
