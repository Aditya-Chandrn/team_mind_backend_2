package com.capstone.mind.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.capstone.mind.backend.DTO.responses.ApiResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleValidExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst().orElse("Invalid data format");
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, errorMessage);
        System.err.println(exception);
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleRateLimiterException(RequestNotPermitted exception) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.TOO_MANY_REQUESTS,
                "Too many requests! Please wait a bit and try again later.");
        System.err.println(exception);
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleCircuitBreakerException(
            io.github.resilience4j.circuitbreaker.CallNotPermittedException ex) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.SERVICE_UNAVAILABLE,
                "Service temporarily unavailable. Please try again later.");
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Body<Void>> handleOtherExceptions(Exception exception) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        System.err.println(exception);
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

}