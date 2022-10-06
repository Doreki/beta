package com.dhgroup.beta.web.dto.PostsDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class PostsUpdateDto {
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;


    @Builder
    public PostsUpdateDto(Long memberId,String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }

//    public static PostsUpdateDto
}
