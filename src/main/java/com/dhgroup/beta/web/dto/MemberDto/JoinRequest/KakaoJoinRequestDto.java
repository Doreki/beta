package com.dhgroup.beta.web.dto.MemberDto.JoinRequest;

import com.dhgroup.beta.domain.member.KakaoMember;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.member.Role;
import com.dhgroup.beta.web.validation.ValidationGroups;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SuperBuilder
@Data
@NoArgsConstructor
public class KakaoJoinRequestDto extends JoinRequestDto {

    private String authId;


    public KakaoMember toEntity() {
        return KakaoMember.builder()
                .authId(authId)
                .nickname(nickname)
                .provider(Provider.KAKAO)
                .role(Role.MEMBER)
                .build();
    }
}
