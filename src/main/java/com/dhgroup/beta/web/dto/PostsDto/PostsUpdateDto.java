package com.dhgroup.beta.web.dto.PostsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostsUpdateDto {
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

}
