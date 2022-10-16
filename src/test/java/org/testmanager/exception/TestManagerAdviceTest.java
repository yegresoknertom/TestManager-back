package org.testmanager.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testmanager.advice.TestManagerAdvice;
import org.testmanager.model.ExceptionDTO;
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
    public void testUserNotFoundException() {
        when(userService.getFreeUser()).thenThrow(FreeUserNotFoundException.class);

        FreeUserNotFoundException freeUserNotFoundException = assertThrows(FreeUserNotFoundException.class, () -> userService.getFreeUser());
        ExceptionDTO exceptionDTO = testManagerAdvice.FreeUserNotFoundHandler(freeUserNotFoundException);
        assertEquals(exceptionDTO.getException(), FreeUserNotFoundException.class.getSimpleName());
    }

}