package com.dhgroup.beta.web.dto.PostsDto;

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
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdDate;



    private PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getMember().getNickname();
        this.likeCount = posts.getLikeCount();
        this.commentCount = posts.getCommentCount();
        this.createdDate = posts.getCreatedDate();
    }

    @Builder
    public PostsResponseDto(Long id, String title, String content, String writer, Integer likeCount, Integer commentCount, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
    }

    public static PostsResponseDto createPostsResponseDto(Posts posts) {
        return new PostsResponseDto(posts);
    }

}
