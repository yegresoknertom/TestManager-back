package org.testmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.model.UserDto;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getFreeUser() {
        log.info("getFreeUser");
        User dbUser = userRepository.findFreeUser().orElseThrow(() -> new FreeUserNotFoundException());
        return UserDto.builder()
                .login(dbUser.getLogin())
                .password(dbUser.getPassword())
                .build();
    }

}
