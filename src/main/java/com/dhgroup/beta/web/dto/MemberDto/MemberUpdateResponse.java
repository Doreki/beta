package com.dhgroup.beta.web.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MemberUpdateResponse {

    Long id;
    String nickname;

    public static MemberUpdateResponse createMemberUpdateResponse(Long id, String nickname) {
        return MemberUpdateResponse.builder().id(id).nickname(nickname).build();
    }
}
