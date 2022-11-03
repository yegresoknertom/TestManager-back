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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.exception.UserAlreadyExistsException;
import org.testmanager.model.ExceptionDTO;
import org.testmanager.model.UserDTO;
import org.testmanager.service.UserService;


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

}
