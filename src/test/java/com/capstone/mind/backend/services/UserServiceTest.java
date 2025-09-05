package com.capstone.mind.backend.services;

import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void registerUser_success() {
        String first = "John";
        String last = "Doe";
        String email = "john.doe@example.com";
        String rawPassword = "Secret123!";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.registerUser(first, last, email, rawPassword);

        assertNotNull(saved);
        assertEquals(first, saved.getFname());
        assertEquals(last, saved.getLname());
        assertEquals(email, saved.getEmail());
        assertNotNull(saved.getPassword());
        assertNotEquals(rawPassword, saved.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, saved.getPassword()));

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).existsByEmail(email);
        inOrder.verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void registerUser_emailAlreadyExists_throws() {
        String email = "taken@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser("A", "B", email, "pwd"));

        assertEquals("Email already exists", ex.getMessage());
        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any());
    }
}