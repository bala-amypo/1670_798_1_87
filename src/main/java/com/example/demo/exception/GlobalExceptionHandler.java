package com.example.demo.exception;

import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handle(RuntimeException ex) {
        return ex.getMessage();
    }
}
