package com.dhgroup.beta.web.dto.PostsDto;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.web.validation.ValidationGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class PostsRequestDto {

    @NotBlank(message = "NOT_BLANK")
    private String title;
    @NotBlank(message = "NOT_BLANK")
    private String content;
    private Long memberId;

    @Builder
    public PostsRequestDto(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Posts toEntity(Member member) {
        return Posts.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

}
