package com.dhgroup.beta.web;

import com.dhgroup.beta.service.UserService;
import com.dhgroup.beta.web.dto.UserCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/user/1h2g2yysh297h2s")
    public Long singUp(UserCreateDto userCreateDto) {
        return userService.signUp(userCreateDto);
    }
}
