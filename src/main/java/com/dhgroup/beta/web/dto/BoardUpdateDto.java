package com.dhgroup.beta.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardUpdateDto {
    private String title;
    private String content;

    @Builder
    public BoardUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
