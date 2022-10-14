package com.dhgroup.beta.web.dto.MemberDto;

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

    public static MemberUpdateResponseDto createMemberUpdateResponse(Long id, String nickname) {
        return MemberUpdateResponseDto.builder().id(id).nickname(nickname).build();
    }
}
