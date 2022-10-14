package com.dhgroup.beta.web.dto.PostsDto;

import com.dhgroup.beta.domain.Posts;
import com.dhgroup.beta.domain.PostsStatus;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikedPostsResponseDto extends PostsResponseDto {
    private LocalDateTime likedDate;


    private LikedPostsResponseDto(Posts posts) {
        super(posts);
        this.likedDate = posts.getLikedDate();
    }

    public static LikedPostsResponseDto createPostsResponseDto(Posts posts) {
        return new LikedPostsResponseDto(posts);
    }

    public void updateLikedDate(LocalDateTime likedDate) {
        this.likedDate = likedDate;
    }

}
