package com.RealWorldApp.exception;

import com.RealWorldApp.exception.custom.CustomBadRequestException;
import com.RealWorldApp.exception.custom.CustomNotFoundException;
import com.RealWorldApp.model.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {

        @ExceptionHandler(CustomBadRequestException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, CustomError> badRequestException(CustomBadRequestException ex){
         return    ex.getCustomErrorMap();
        }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Map<String, CustomError> notFoundException(CustomNotFoundException ex){
        return    ex.getCustomErrorMap();
    }
}
