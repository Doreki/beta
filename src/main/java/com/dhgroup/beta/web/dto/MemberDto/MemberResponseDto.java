package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private  Long id;
    private String googleId;
    private String nickname;

    private MemberResponseDto(Long id, String googleId, String nickname) {
        this.id = id;
        this.googleId = googleId;
        this.nickname = nickname;
    }

    public static MemberResponseDto createMemberResponseDto(Member member) {
        return new MemberResponseDto(member.getId(), member.getGoogleId(), member.getNickname());
    }
}
