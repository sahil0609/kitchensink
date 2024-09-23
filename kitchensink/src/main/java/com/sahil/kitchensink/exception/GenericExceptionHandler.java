package com.sahil.kitchensink.exception;

import com.sahil.kitchensink.model.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(basePackages = "com.sahil.kitchensink.controller")
@Slf4j
public class GenericExceptionHandler {


    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAuthorizationException(AuthorizationException e) {
        log.error("Exception occurred: ", e);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleMemberNotFoundException(MemberNotFoundException e) {
        log.error("Member not found: ", e);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthenticationException(AuthenticationException e) {
        log.error("Exception occurred: ", e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorData> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorData data = new ErrorData();
        List<String> errorMessage = new ArrayList<>();
        for(var errors : e.getFieldErrors()) {
            log.error("Validation failed: {}", errors.getDefaultMessage());
            errorMessage.add(errors.getDefaultMessage());
        }
        for(var errors : e.getGlobalErrors()) {
            log.error("Validation failed: {}", errors.getDefaultMessage());
            errorMessage.add(errors.getDefaultMessage());
        }
        data.setMessage(errorMessage);
        data.setCode("inputError.invalid");
        data.setRetryable(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleGenericException(Exception e) {
        log.error("Exception occurred: ", e);
    }

}
