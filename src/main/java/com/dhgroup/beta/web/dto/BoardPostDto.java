package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostDto {
    private String title;
    private String content;
    private Member member;

    @Builder
    public BoardPostDto(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

}
