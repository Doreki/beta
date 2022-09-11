package com.dhgroup.beta.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
