package com.capstone.mind.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.capstone.mind.backend.DTO.data.AuthResponseData;
import com.capstone.mind.backend.DTO.requests.LoginRequest;
import com.capstone.mind.backend.DTO.requests.SignupRequest;
import com.capstone.mind.backend.DTO.responses.ApiResponse;
import com.capstone.mind.backend.services.AuthService;
import com.capstone.mind.backend.services.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;
    private final JwtService jwtService;

    public UserController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/check")
    @CircuitBreaker(name = "externalService")
    @RateLimiter(name = "externalService")
    public ResponseEntity<ApiResponse<Void>> checkServer() {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK, "Server pinged successfully");
        System.out.println("Server pinged successfully");
        return ResponseEntity.ok(response);
    }

    @RateLimiter(name = "externalService")
    @CircuitBreaker(name = "externalService")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse.Body<AuthResponseData>> login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<AuthResponseData> response = authService.Login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @RateLimiter(name = "externalService")
    @CircuitBreaker(name = "externalService")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse.Body<AuthResponseData>> signup(@RequestBody @Valid SignupRequest request) {
        ApiResponse<AuthResponseData> response = authService.Signup(request.getEmail(), request.getFname(),
                request.getLname(), request.getPassword());
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @RateLimiter(name = "externalService")
    @CircuitBreaker(name = "externalService")
    @GetMapping("/auth")
    public ResponseEntity<Claims> validateToken(HttpServletRequest request) {
        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        try {
            Claims claims = jwtService.getAllClaims(token);
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
    }

}
