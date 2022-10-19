package com.dhgroup.beta.web.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String memberName;
    private String password;

    public static MemberLoginRequestDto createMemberLoginRequestDto(String memberName, String password) {
        return MemberLoginRequestDto.builder()
                .memberName(memberName)
                .password(password)
                .build();
    }
}
