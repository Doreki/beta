package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.member.Member;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private  Long id;
    private String userTag;

    public static MemberResponseDto createMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .userTag(member.getUserTag())
                .build();
    }
}
