package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Member;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
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
