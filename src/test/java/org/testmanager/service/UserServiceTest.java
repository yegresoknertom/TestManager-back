package org.testmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.testmanager.mapper.UserMapper;
import org.testmanager.model.UserDTO;
import org.testmanager.model.UserListDTO;
import org.testmanager.model.entity.User;
import org.testmanager.repository.UserRepository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    void getUsersTest() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        Page<User> resultPage = new PageImpl<>(Arrays.asList(user));
        when(userRepository.findAll(PageRequest.of(0, 1))).thenReturn(resultPage);

        UserDTO userDTO = new UserDTO().setLogin("testLogin").setPassword("Qwerty123");
        when(userMapper.entityToDto(user))
                .thenReturn(userDTO);
        UserListDTO userListDTO = userService.getUsersPaginated(0, 1);
        assertAll(
                () -> assertTrue(userListDTO.getUsers().get(0).getLogin() =="testLogin", "Logins do not match"),
                () -> assertTrue(userListDTO.getUsers().get(0).getPassword() =="Qwerty123", "Passwords do not match"),
                () -> assertTrue(userListDTO.getPagination().getPageNumber() == 0, "Page number do not match"),
                () -> assertTrue(userListDTO.getPagination().getPageSize() == 1, "Page size do not match"),
                () -> assertTrue(userListDTO.getPagination().getTotalPages() == 1, "Total pages do not match"),
                () -> assertTrue(userListDTO.getPagination().getTotalElements() == 1, "Total elements do not match")
        );
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

    @Test
    void editUserTest() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        UserDTO userDTO = new UserDTO().setLogin("testLogin").setPassword("Qwerty123");
        when(userMapper.entityToDto(user))
                .thenReturn(userDTO);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertTrue(userService.editUser(userDTO).getLogin() =="testLogin", "Logins do not match");
        assertTrue(userService.editUser(userDTO).getPassword() == "Qwerty123", "Passwords do not match");
    }

    @Test
    void deleteUserTest() {
        User user = new User();
        user.setLogin("testLogin");
        user.setPassword("Qwerty123");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(java.util.Optional.of(user));

        userService.deleteUser("testLogin");
        verify(userRepository, times(1)).delete(user);
    }

}