package com.dhgroup.beta.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostsUpdateDto {
    private String title;
    private String content;


    @Builder
    public PostsUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
