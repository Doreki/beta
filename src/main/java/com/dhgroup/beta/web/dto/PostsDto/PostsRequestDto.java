package com.dhgroup.beta.web.dto.PostsDto;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.PostsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostsRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Long memberId;

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Posts toEntity(Member member) {
        return Posts.builder()
                .title(title)
                .content(content)
                .member(member)
                .likeCount(0)
                .commentCount(0)
                .status(PostsStatus.CREATED)
                .build();
    }

}
