package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.repository.UserRepository;
import com.dhgroup.beta.web.dto.UserCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long signUp(UserCreateDto userCreateDto) {

        return userRepository.save(userCreateDto.toEntity()).getId();
    }
}
