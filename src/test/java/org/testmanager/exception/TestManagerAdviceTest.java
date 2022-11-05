package org.testmanager.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testmanager.advice.TestManagerAdvice;
import org.testmanager.model.ExceptionDTO;
import org.testmanager.model.UserDTO;
import org.testmanager.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestManagerAdviceTest {


    @Mock
    private UserService userService;

    @InjectMocks
    private TestManagerAdvice testManagerAdvice;

    @Test
    public void testFreeUserNotFoundException() {
        when(userService.getFreeUser()).thenThrow(FreeUserNotFoundException.class);

        FreeUserNotFoundException freeUserNotFoundException = assertThrows(FreeUserNotFoundException.class, () -> userService.getFreeUser());
        ExceptionDTO exceptionDTO = testManagerAdvice.FreeUserNotFoundHandler(freeUserNotFoundException);
        assertEquals(exceptionDTO.getException(), FreeUserNotFoundException.class.getSimpleName());
    }

    @Test
    public void testUserNotFoundException() {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.editUser(user)).thenThrow(UserNotFoundException.class);

        UserNotFoundException UserNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.editUser(user));
        ExceptionDTO exceptionDTO = testManagerAdvice.UserNotFoundHandler(UserNotFoundException);
        assertEquals(exceptionDTO.getException(), UserNotFoundException.class.getSimpleName());
    }

    @Test
    public void testUserAlreadyExistsException() {
        UserDTO user = new UserDTO().setLogin("user1").setPassword("Qwerty123");
        when(userService.createUser(user)).thenThrow(UserAlreadyExistsException.class);

        UserAlreadyExistsException userAlreadyExistsException = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
        ExceptionDTO exceptionDTO = testManagerAdvice.UserAlreadyExistsHandler(userAlreadyExistsException);
        assertEquals(exceptionDTO.getException(), UserAlreadyExistsException.class.getSimpleName());
    }

}