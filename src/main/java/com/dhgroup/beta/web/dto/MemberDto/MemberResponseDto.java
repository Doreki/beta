package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private  Long id;
    private String googleId;
    private String nickname;

    public static MemberResponseDto createMemberResponseDto(Member member) {
        return new MemberResponseDto(member.getId(), member.getGoogleId(), member.getNickname());
    }
}
