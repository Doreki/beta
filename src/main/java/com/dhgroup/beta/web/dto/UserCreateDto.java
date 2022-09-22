package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
public class UserCreateDto {

    private final String googleId;
    private final String nickname;

    public User toEntity() {
        return User.builder()
                .googleId(googleId)
                .nickName(nickname)
                .build();
    }
}
