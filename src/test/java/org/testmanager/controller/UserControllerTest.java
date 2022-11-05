package org.testmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.exception.UserAlreadyExistsException;
import org.testmanager.exception.UserNotFoundException;
import org.testmanager.model.ExceptionDTO;
import org.testmanager.model.Pagination;
import org.testmanager.model.UserDTO;
import org.testmanager.model.UserListDTO;
import org.testmanager.service.UserService;

import java.util.Arrays;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void  getFreeUserTest() throws Exception {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.getFreeUser()).thenReturn(user);

        mockMvc.perform(get("/users/freeuser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("user1"))
                .andExpect(jsonPath("password").value("Qwerty123"));

        verify(userService, times(1)).getFreeUser();
    }

    @Test
    public void  getUsersTest() throws Exception {
        UserListDTO userListDTO = new UserListDTO();
        UserDTO user1 = new UserDTO().setLogin("user1").setPassword("qwerty");
        UserDTO user2 = new UserDTO().setLogin("user2").setPassword("qwerty");
        userListDTO
                .setUsers(Arrays.asList(user1, user2))
                .setPagination(new Pagination(0, 10, 1, 2));
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
        queryParams.add("page", "0");
        queryParams.add("size", "10");
        when(userService.getUsersPaginated(0, 10)).thenReturn(userListDTO);

        mockMvc.perform(get("/users").queryParams(queryParams))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("users").isArray())
                .andExpect(jsonPath("users[0].login").value("user1"))
                .andExpect(jsonPath("users[0].password").value("qwerty"))
                .andExpect(jsonPath("users[1].login").value("user2"))
                .andExpect(jsonPath("users[1].password").value("qwerty"))
                .andExpect(jsonPath("pagination.pageNumber").value("0"))
                .andExpect(jsonPath("pagination.pageSize").value("10"))
                .andExpect(jsonPath("pagination.totalPages").value("1"))
                .andExpect(jsonPath("pagination.totalElements").value("2"));

        verify(userService, times(1)).getUsersPaginated(0, 10);
    }

    @Test
    public void  getUsersTestDefaultPagination() throws Exception {
        UserListDTO userListDTO = new UserListDTO();
        UserDTO user1 = new UserDTO().setLogin("user1").setPassword("qwerty");
        UserDTO user2 = new UserDTO().setLogin("user2").setPassword("qwerty");
        userListDTO
                .setUsers(Arrays.asList(user1, user2))
                .setPagination(new Pagination(0, 50, 1, 2));;
        when(userService.getUsersPaginated(0, 50)).thenReturn(userListDTO);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("users").isArray())
                .andExpect(jsonPath("users[0].login").value("user1"))
                .andExpect(jsonPath("users[0].password").value("qwerty"))
                .andExpect(jsonPath("users[1].login").value("user2"))
                .andExpect(jsonPath("users[1].password").value("qwerty"))
                .andExpect(jsonPath("pagination.pageNumber").value("0"))
                .andExpect(jsonPath("pagination.pageSize").value("50"))
                .andExpect(jsonPath("pagination.totalPages").value("1"))
                .andExpect(jsonPath("pagination.totalElements").value("2"));

        verify(userService, times(1)).getUsersPaginated(0, 50);
    }

    @Test
    public void  whenNoUsers_returnEmptyArrayOfUsers() throws Exception {
        UserListDTO userListDTO = new UserListDTO();
        userListDTO
                .setUsers(Arrays.asList())
                .setPagination(new Pagination(0, 50, 1, 2));
        when(userService.getUsersPaginated(0, 50)).thenReturn(userListDTO);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("users").isArray())
                .andExpect(jsonPath("users").isEmpty())
                .andExpect(jsonPath("pagination.pageNumber").value("0"))
                .andExpect(jsonPath("pagination.pageSize").value("50"))
                .andExpect(jsonPath("pagination.totalPages").value("1"))
                .andExpect(jsonPath("pagination.totalElements").value("2"));

        verify(userService, times(1)).getUsersPaginated(0, 50);
    }

    @Test
    public void whenFreeUserNotFound_thenReturn404AndError() throws Exception {
        when(userService.getFreeUser()).thenThrow(new FreeUserNotFoundException());

        MvcResult mvcResult = mockMvc.perform(get("/users/freeuser"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionDTO exceptionDTO = new ExceptionDTO("FreeUserNotFoundException", "Free user not found");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionDTO);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void  createUserTest() throws Exception {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("user1"))
                .andExpect(jsonPath("password").value("Qwerty123"));

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void whenUserAlreadyExists_thenReturn409AndError() throws Exception {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.createUser(user)).thenThrow(new UserAlreadyExistsException());

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();

        ExceptionDTO exceptionDTO = new ExceptionDTO("UserAlreadyExistsException", "User already exists");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionDTO);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void whenNullValue_thenReturns400() throws Exception {
        UserDTO user = new UserDTO().setLogin(null).setPassword(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValueLessThanMin_thenReturns400() throws Exception {
        UserDTO user = new UserDTO().setLogin("").setPassword("Qwerty");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValueMoreThanMax_thenReturns400() throws Exception {
        UserDTO user = new UserDTO().setLogin("VeryLongLooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooogin")
                .setPassword("Qwerty");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void  editUserTest() throws Exception {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.editUser(user)).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("user1"))
                .andExpect(jsonPath("password").value("Qwerty123"));

        verify(userService, times(1)).editUser(user);
    }

    @Test
    public void editUserWhenUserNotFound_thenReturn404AndError() throws Exception {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.editUser(user)).thenThrow(new UserNotFoundException());

        MvcResult mvcResult = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionDTO exceptionDTO = new ExceptionDTO("UserNotFoundException", "User not found");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionDTO);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void  deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/login"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser("login");
    }

}
