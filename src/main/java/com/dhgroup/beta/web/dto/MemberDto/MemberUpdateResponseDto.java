package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateResponseDto {

    Long id;
    String nickname;

    public static MemberUpdateResponseDto createMemberUpdateResponse(Member member) {
        return MemberUpdateResponseDto.builder().id(member.getId()).nickname(member.getNickname()).build();
    }
}
