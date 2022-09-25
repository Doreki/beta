package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberCreateDto {

    private final String googleId;
    private final String nickname;

    public Member toEntity() {
        return Member.builder()
                .googleId(googleId)
                .nickName(nickname)
                .build();
    }
}
