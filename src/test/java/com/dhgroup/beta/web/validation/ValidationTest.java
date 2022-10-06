package com.dhgroup.beta.web.validation;

import com.dhgroup.beta.web.dto.MemberDto.MemberRequestDto;
import com.dhgroup.beta.web.dto.PostsDto.PostsRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class ValidationTest {
    private static ValidatorFactory validatorFactory;

    private static Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
     public void 닉네임_공백() throws Exception{
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("1", "");
        //when
        Set<ConstraintViolation<MemberRequestDto>> violations = validator.validate(memberRequestDto, ValidationGroups.NotBlankGroup.class);
        //then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다");
        });
    }

    @Test
    public void 닉네임_사이즈() throws Exception{
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("1", "홍");
        //when
        Set<ConstraintViolation<MemberRequestDto>> violations = validator.validate(memberRequestDto, ValidationGroups.SizeGroup.class);
        //then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("2자에서 8자 사이로 입력하시오");
        });
    }

    @Test
    public void 닉네임_패턴() throws Exception{
        //given
        MemberRequestDto memberRequestDto = createMemberRequestDto("1", "!!");
        //when
        Set<ConstraintViolation<MemberRequestDto>> violations = validator.validate(memberRequestDto, ValidationGroups.PatternCheckGroup.class);
        //then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("특수문자, 공백은 입력할 수 없습니다.");
        });
    }

    @Test
    public void 글_공백() throws Exception{
        //given
        PostsRequestDto postsRequestDto = createPostsRequestDto("", "");
        //when
        Set<ConstraintViolation<PostsRequestDto>> violations = validator.validate(postsRequestDto);
        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isEqualTo(2);
    }

    private static PostsRequestDto createPostsRequestDto(String title, String content) {
        return PostsRequestDto.builder().title(title).content(content).build();
    }

    private static MemberRequestDto createMemberRequestDto(String googleId, String nickname) {
        return MemberRequestDto.builder().googleId(googleId).nickname(nickname).build();
    }
}
