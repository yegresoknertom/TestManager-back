package org.testmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getFreeUser() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        when(userRepository.findFreeUser()).thenReturn(java.util.Optional.of(user));

        assertTrue(userService.getFreeUser().getLogin() =="testLogin", "Logins do not match");
        assertTrue(userService.getFreeUser().getPassword() == "Qwerty123", "Passwords do not match");
    }

}