package com.capstone.mind.backend.services;

import com.capstone.mind.backend.DTO.data.AuthResponseData;
import com.capstone.mind.backend.DTO.responses.ApiResponse;
import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User("John", "Doe", "john@example.com", "hashedPwd");
    }

    @Test
    void login_success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("RawPass1!", "hashedPwd")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        ApiResponse<AuthResponseData> resp = authService.Login("john@example.com", "RawPass1!");

        assertEquals(HttpStatus.OK, resp.getStatus());
        assertNotNull(resp.getBody().getData());
        assertEquals("jwt-token", resp.getBody().getData().getToken());
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_invalidPassword() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("bad", "hashedPwd")).thenReturn(false);

        ApiResponse<AuthResponseData> resp = authService.Login("john@example.com", "bad");

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatus());
        assertNull(resp.getBody().getData());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_userNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ApiResponse<AuthResponseData> resp = authService.Login("missing@example.com", "pwd");

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatus());
        assertNull(resp.getBody().getData());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_tokenGenerationFailure() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("RawPass1!", "hashedPwd")).thenReturn(true);
        when(jwtService.generateToken(user)).thenThrow(new RuntimeException("JWT error"));

        ApiResponse<AuthResponseData> resp = authService.Login("john@example.com", "RawPass1!");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatus());
        assertNull(resp.getBody().getData());
    }

    @Test
    void signup_success() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Pass123!")).thenReturn("encPwd");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("new-token");

        ApiResponse<AuthResponseData> resp =
                authService.Signup("new@example.com", "Jane", "Doe", "Pass123!");

        assertEquals(HttpStatus.OK, resp.getStatus());
        assertNotNull(resp.getBody().getData());
        assertEquals("new@example.com", resp.getBody().getData().getEmail());
        assertEquals("new-token", resp.getBody().getData().getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_existingUser() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        ApiResponse<AuthResponseData> resp =
                authService.Signup("john@example.com", "John", "Doe", "Pass123!");

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatus());
        assertNull(resp.getBody().getData());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void signup_repositoryFailure() {
        when(userRepository.findByEmail("err@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Pwd1")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB down"));

        ApiResponse<AuthResponseData> resp =
                authService.Signup("err@example.com", "A", "B", "Pwd1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatus());
        assertNull(resp.getBody().getData());
    }

    @Test
    void signup_tokenGenerationFailure() {
        when(userRepository.findByEmail("tok@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Pwd1")).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenThrow(new RuntimeException("JWT gen fail"));

        ApiResponse<AuthResponseData> resp =
                authService.Signup("tok@example.com", "T", "K", "Pwd1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatus());
        assertNull(resp.getBody().getData());
    }
}