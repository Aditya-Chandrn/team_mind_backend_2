package com.capstone.mind.backend.DTO.responses;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

    public static class Body<T> {
        private final String message;
        private final T data;

        public Body(String message, T data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }

    private final HttpStatus status;
    private final Body<T> body;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.body = new Body<>(message, data);
    }

    public ApiResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Body<T> getBody() {
        return body;
    }
}
