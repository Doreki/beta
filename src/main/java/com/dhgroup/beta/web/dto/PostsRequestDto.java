package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostsRequestDto {
    private String title;
    private String content;
    private Member member;

    @Builder
    public PostsRequestDto(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

}
