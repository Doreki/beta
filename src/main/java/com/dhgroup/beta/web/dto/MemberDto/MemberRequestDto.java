package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.Member;
import com.dhgroup.beta.web.validation.ValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class MemberRequestDto {

    private String googleId;

    @NotBlank(message = "NOT_BLANK",groups = ValidationGroups.NotBlankGroup.class)
    @Size(min = 2,max =8,message = "LENGTH(2,8)",groups = ValidationGroups.SizeGroup.class)
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$",message = "NOT_SIGN",groups = ValidationGroups.PatternCheckGroup.class)
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