package org.testmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.exception.UserAlreadyExistsException;
import org.testmanager.mapper.UserMapper;
import org.testmanager.model.UserDTO;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getFreeUser() {
        log.info("getFreeUser");
        User dbUser = userRepository.findFreeUser().orElseThrow(() -> new FreeUserNotFoundException());
        return userMapper.entityToDto(dbUser);
    }

    public UserDTO createUser(UserDTO userDto) {
        log.info("create user");
        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDto)));
        }
    }

}
