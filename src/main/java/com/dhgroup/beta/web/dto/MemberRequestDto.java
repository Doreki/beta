package com.dhgroup.beta.web.dto;

import com.dhgroup.beta.domain.Member;
import lombok.*;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class MemberRequestDto {

    private String googleId;
    @Pattern(regexp = "^[0-9a-zA-Z가-힇]{2,10}$",message = "특수문자, 공백을 제외하고 2자 이상 10자 이하로 입력하세요.")
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
