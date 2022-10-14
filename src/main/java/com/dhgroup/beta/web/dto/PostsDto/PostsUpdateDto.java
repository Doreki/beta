package com.dhgroup.beta.web.dto.PostsDto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostsUpdateDto {
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

}
