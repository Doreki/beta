package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdDate;


    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getMember().getNickname();
        this.likeCnt = posts.getLikeCnt();
        this.commentCnt = posts.getCommentCnt();
        this.createdDate = posts.getCreatedDate();
    }

    @Builder
    public PostsResponseDto(Long id, String title, String content, String writer, Integer likeCnt, Integer commentCnt, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.createdDate = createdDate;
    }
}
