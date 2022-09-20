package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Board;
import com.dhgroup.beta.domain.User;
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
    private User user;

    @Builder
    public BoardPostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
    //생성자를 통해 writer를 주입 못하도록 차단, session값을 통해서만 writer를 얻어오기 위함
    public void setUser(User user) {
        this.user = user;
    }

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

}
