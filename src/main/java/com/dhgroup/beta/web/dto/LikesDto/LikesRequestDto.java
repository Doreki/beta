package com.dhgroup.beta.web.dto.LikesDto;

import com.dhgroup.beta.domain.Likes;
import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LikesRequestDto {

    private Long memberId;
    private Long postsId;

    @Builder
    public LikesRequestDto(Long memberId, Long postsId) {
        this.memberId = memberId;
        this.postsId = postsId;
    }

    public Likes toEntity(Posts posts, Member member){
        return Likes.createLikes(posts, member);
    }
}
