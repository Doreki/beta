package com.dhgroup.beta.web.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String username;
    private String password;

    public static MemberLoginRequestDto createMemberLoginRequestDto(String memberName, String password) {
        return MemberLoginRequestDto.builder()
                .username(memberName)
                .password(password)
                .build();
    }
}
