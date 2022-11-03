package org.testmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testmanager.mapper.UserMapper;
import org.testmanager.model.UserDTO;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getFreeUserTest() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        UserDTO userDTO = new UserDTO().setLogin("testLogin").setPassword("Qwerty123");
        when(userMapper.entityToDto(user))
                .thenReturn(userDTO);
        when(userRepository.findFreeUser()).thenReturn(java.util.Optional.of(user));

        assertTrue(userService.getFreeUser().getLogin() =="testLogin", "Logins do not match");
        assertTrue(userService.getFreeUser().getPassword() == "Qwerty123", "Passwords do not match");
    }

    @Test
    void createUserTest() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        UserDTO userDTO = new UserDTO().setLogin("testLogin").setPassword("Qwerty123");
        when(userMapper.entityToDto(user))
                .thenReturn(userDTO);
        when(userMapper.dtoToEntity(userDTO))
                .thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        assertTrue(userService.createUser(userDTO).getLogin() =="testLogin", "Logins do not match");
        assertTrue(userService.createUser(userDTO).getPassword() == "Qwerty123", "Passwords do not match");
    }

}