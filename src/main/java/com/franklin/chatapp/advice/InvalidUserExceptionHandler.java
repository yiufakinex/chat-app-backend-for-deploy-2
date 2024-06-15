package com.franklin.chatapp.advice;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.franklin.chatapp.exception.InvalidUserException;
import com.franklin.chatapp.util.Response;

@ControllerAdvice
public class InvalidUserExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<HashMap<String, Object>> handleInvalidUserException(InvalidUserException e) {
        return Response.unauthorized();
    }
}
