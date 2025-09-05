package com.capstone.mind.backend.services;
import com.capstone.mind.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        user = new User();
        user.setEmail("test@example.com");
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void testValidateToken() {
        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.validateToken(token, user);

        assertTrue(isValid);
    }
}
