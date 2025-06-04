package com.healthconnect.summary.controller;

import com.healthconnect.summary.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        ErrorDto error = ErrorDto.builder()
                .code("VALIDATION_ERROR")
                .message("Invalid input: " + ex.getBindingResult().getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.badRequest().body(Collections.singletonMap("errors", Collections.singletonList(error)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        ErrorDto error = ErrorDto.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred. Please try again later.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("errors", Collections.singletonList(error)));
    }
}