package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Getter
public class BoardPostDto {
    private String title;
    private String content;
    private String writer;

    @Builder
    public BoardPostDto(String title, String content,String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
    //생성자를 통해 writer를 주입 못하도록 차단, session값을 통해서만 writer를 얻어오기 위함
    public void setWriter(String writer) {
        this.writer = writer;
    }

    /*
        웹으로 부터 받아온 정보를 DB에 넣을 수있는 형태로 가공한다.
         */
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }

    @Override
    public String toString() {
        return "BoardPostDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPostDto that = (BoardPostDto) o;
        return Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(writer, that.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, writer);
    }
}
