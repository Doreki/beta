package com.dhgroup.beta.web.dto.PostsDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class PostsUpdateDto {
    private String googleId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;


    @Builder
    public PostsUpdateDto(String googleId,String title, String content) {
        this.googleId = googleId;
        this.title = title;
        this.content = content;
    }

//    public static PostsUpdateDto
}
