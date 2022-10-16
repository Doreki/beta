package com.dhgroup.beta.web.dto.MemberDto;

import com.dhgroup.beta.domain.member.Member;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.web.validation.ValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoJoinRequestDto {

    private String authId;

    @NotBlank(groups = ValidationGroups.NotBlankGroup.class)
    @Size(min = 2,max =8,message = "2자에서 8자 사이로 입력하시오",groups = ValidationGroups.SizeGroup.class)
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$",message = "특수문자, 공백은 입력할 수 없습니다.",groups = ValidationGroups.PatternCheckGroup.class)
    private String nickname;


    public Member toEntity() {
        return Member.builder()
                .authId(authId)
                .nickname(nickname)
                .provider(Provider.KAKAO)
                .build();
    }
}
