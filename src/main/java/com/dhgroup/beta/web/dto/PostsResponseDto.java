package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class PostsResponseDto {
    private Posts posts;
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdDate;


//    public void setPosts(Posts posts) {
//        this.posts = posts;
//    }

    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getMember().getNickname();
        this.likeCnt = posts.getLikeCnt();
        this.commentCnt = posts.getCommentCnt();
        this.createdDate = posts.getCreatedDate();
    }
}
