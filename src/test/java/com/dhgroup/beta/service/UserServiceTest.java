package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.User;
import com.dhgroup.beta.domain.repository.UserRepository;
import com.dhgroup.beta.web.dto.UserCreateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
     public void 회원가입() throws Exception{
        User user = createUser();
        //given
        given(userRepository.save(any(User.class))).willReturn(user);
        //when
        userService.signUp(new UserCreateDto("1", "글쓴이"));
        //then
        verify(userRepository).save(any(User.class));
    }

    private static User createUser() {
        return User.builder()
                .googleId("1")
                .nickName("글쓴이")
                .build();
    }
}
