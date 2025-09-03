package com.capstone.mind.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.capstone.mind.backend.DTO.responses.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleValidExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
                .findFirst().orElse("Invalid data format");
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, errorMessage);
        System.err.println(exception);
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleOtherExceptions(Exception exception) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        System.err.println(exception);
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }
}