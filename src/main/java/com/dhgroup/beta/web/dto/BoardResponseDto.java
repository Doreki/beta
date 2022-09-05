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
    private Integer viewCnt;
    private Integer commentCnt;

    @Builder

    public BoardResponseDto(String title, String content, String writer, Integer viewCnt, Integer commentCnt) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewCnt = viewCnt;
        this.commentCnt = commentCnt;
    }
}
