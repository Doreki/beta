package com.dhgroup.beta.web.dto.LikesDto;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikesRequestDto {

    private Long memberId;
    private Long postsId;

    public Likes toEntity(Posts posts, Member member){
        return Likes.createLikes(posts, member);
    }
}
