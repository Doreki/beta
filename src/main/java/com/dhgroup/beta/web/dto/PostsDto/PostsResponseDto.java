package com.dhgroup.beta.web.dto.PostsDto;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.PostsStatus;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostsResponseDto {
    private Long postsId;
    private String title;
    private String content;
    private String writer;
    private Integer likeCount;
    private Integer commentCount;

    private PostsStatus status;

    private boolean isLiked;

    private LocalDateTime date;

    protected PostsResponseDto(Posts posts) {
        this.postsId = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getMember().getNickname();
        this.likeCount = posts.getLikeCount();
        this.commentCount = posts.getCommentCount();
        this.status = posts.getStatus();
        this.date = posts.getCreatedDate();
    }

    public static PostsResponseDto createPostsResponseDto(Posts posts) {
        return new PostsResponseDto(posts);
    }

    public void updateIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

}
