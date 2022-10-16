package org.testmanager.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.testmanager.exception.FreeUserNotFoundException;
import org.testmanager.model.ExceptionDTO;

@ControllerAdvice
public class TestManagerAdvice {

    @ResponseBody
    @ExceptionHandler(FreeUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO FreeUserNotFoundHandler(FreeUserNotFoundException ex) {
        return new ExceptionDTO(ex.getClass().getSimpleName(), ex.getMessage());
    }

}
