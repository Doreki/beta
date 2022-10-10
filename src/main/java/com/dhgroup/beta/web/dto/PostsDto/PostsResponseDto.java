package com.dhgroup.beta.web.dto.PostsDto;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.PostsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Integer likeCount;
    private Integer commentCount;

    private PostsStatus status;

    private boolean isLiked;

    private LocalDateTime date;

    private LocalDateTime likedTime;

    private PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getMember().getNickname();
        this.likeCount = posts.getLikeCount();
        this.commentCount = posts.getCommentCount();
        this.status = posts.getStatus();
        this.isLiked = posts.isLiked();

        if(this.status == PostsStatus.CREATED)
            this.date = posts.getCreatedDate();
        else
            this.date = posts.getModifiedDate();

        this.likedTime = posts.getLikedDate();
    }

    public static PostsResponseDto createPostsResponseDto(Posts posts) {
        return new PostsResponseDto(posts);
    }

}
