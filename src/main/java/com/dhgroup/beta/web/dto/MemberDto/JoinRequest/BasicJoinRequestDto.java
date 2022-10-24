package com.dhgroup.beta.web.dto.MemberDto.JoinRequest;

import com.dhgroup.beta.domain.member.BasicMember;
import com.dhgroup.beta.domain.member.Provider;
import com.dhgroup.beta.domain.member.Role;
import com.dhgroup.beta.web.validation.ValidationGroups;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SuperBuilder
@Data
@NoArgsConstructor
public class BasicJoinRequestDto extends JoinRequestDto {

    @Pattern(regexp = "^[0-9a-zA-Z~!@#$%^&*_]*$",message = "한글은 입력할 수 없습니다.",groups = ValidationGroups.PatternCheckGroup.class)
    @NotBlank(message = "아이디는 비워 둘 수 없습니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String username;
    @NotBlank(message = "비밀번호는 비워 둘 수 없습니다", groups = ValidationGroups.NotBlankGroup.class)
    @Size(min = 8, max = 16, message = "비밀번호는 8자 부터 16자까지 입력하여야 합니다", groups = ValidationGroups.SizeGroup.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*_])[a-zA-Z\\d-~!@#$%^&*_]*$",
             message = "비밀번호는 특수기호, 영문, 숫자를 모두 포함해야합니다.",
             groups = ValidationGroups.PatternCheckGroup.class)
    private String password;




    public BasicMember toEntity() {
        return BasicMember.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password))
                .nickname(nickname)
                .provider(Provider.BASIC)
                .role(Role.MEMBER)
                .build();
    }
}
