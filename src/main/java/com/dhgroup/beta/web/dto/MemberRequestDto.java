package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberRequestDto {

    private String googleId;
    private String nickname;

    @Builder
    public MemberRequestDto(String googleId, String nickname) {
        this.googleId = googleId;
        this.nickname = nickname;
    }

    public Member toEntity() {
        return Member.builder()
                .googleId(googleId)
                .nickname(nickname)
                .build();
    }
}
