package com.capstone.mind.backend.controller;

import com.capstone.mind.backend.DTO.data.AuthResponseData;
import com.capstone.mind.backend.DTO.responses.ApiResponse;
import com.capstone.mind.backend.services.AuthService;
import com.capstone.mind.backend.services.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests UserController endpoints.
 * Adjust jsonPath expectations if ApiResponse serialization differs.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("GET /api/check returns OK with message")
    void checkServer_ok() throws Exception {
        mockMvc.perform(get("/api/check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Server pinged successfully"));
    }

    @Test
    @DisplayName("POST /api/login success")
    void login_success() throws Exception {
        AuthResponseData data = new AuthResponseData("John", "Doe", "john@example.com", "jwt-token");
        ApiResponse<AuthResponseData> serviceResponse =
                new ApiResponse<>(HttpStatus.OK, "Login successful", data);

        Mockito.when(authService.Login("john@example.com", "Pass123!"))
                .thenReturn(serviceResponse);

        String requestJson = """
                {
                  "email":"john@example.com",
                  "password":"Pass123!"
                }
                """;

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /api/register success")
    void register_success() throws Exception {
        AuthResponseData data = new AuthResponseData("Jane", "Doe", "jane@example.com", "new-token");
        ApiResponse<AuthResponseData> serviceResponse =
                new ApiResponse<>(HttpStatus.OK, "Signup successful", data);

        Mockito.when(authService.Signup("jane@example.com", "Jane", "Doe", "StrongPwd1"))
                .thenReturn(serviceResponse);

        String requestJson = """
                {
                  "email":"jane@example.com",
                  "fname":"Jane",
                  "lname":"Doe",
                  "password":"StrongPwd1"
                }
                """;

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signup successful"))
                .andExpect(jsonPath("$.data.email").value("jane@example.com"))
                .andExpect(jsonPath("$.data.token").value("new-token"));
    }

    @Test
    @DisplayName("GET /api/auth valid token returns 200")
    void auth_validate_success() throws Exception {
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(jwtService.getAllClaims("validtoken")).thenReturn(claims);

        mockMvc.perform(get("/api/auth")
                        .header("Authorization", "Bearer validtoken"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/auth missing header returns 401")
    void auth_validate_missingHeader() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/auth invalid token returns 401")
    void auth_validate_invalidToken() throws Exception {
        Mockito.when(jwtService.getAllClaims(anyString()))
                .thenThrow(new RuntimeException("bad token"));

        mockMvc.perform(get("/api/auth")
                        .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isUnauthorized());
    }
}