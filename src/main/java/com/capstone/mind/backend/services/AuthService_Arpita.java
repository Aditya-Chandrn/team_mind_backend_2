/*package com.example.auth.service;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.RSAUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Signup: save new user
    public String signup(String firstName, String lastName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(firstName, lastName, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return "User registered successfully!";
    }


    // Login: verify user & return JWT
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return JwtUtil.generateToken(user.getEmail());
    }
}*/

package com.capstone.mind.backend.services;

import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;
import com.capstone.mind.backend.util.JwtUtil;
import com.capstone.mind.backend.util.RSAUtil;
import com.capstone.mind.backend.util.TokenStore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService_Arpita {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RSAUtil rsaUtil;
    private final TokenStore tokenStore;
    private final JwtUtil jwtUtil;
    private final MockExternalAuthService externalAuthService;

    public AuthService_Arpita(UserRepository userRepository, RSAUtil rsaUtil, TokenStore tokenStore,
                              JwtUtil jwtUtil, MockExternalAuthService externalAuthService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.rsaUtil = rsaUtil;
        this.tokenStore = tokenStore;
        this.jwtUtil = jwtUtil;
        this.externalAuthService = externalAuthService;
    }

    public String signup(String firstName, String lastName, String email, String encryptedPassword) {
        try {
            // Decrypt the password first
            String decryptedPassword = rsaUtil.decrypt(encryptedPassword);

            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists");
            }

            User user = new User(firstName, lastName, email, passwordEncoder.encode(decryptedPassword));
            userRepository.save(user);
            return "User registered successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Password decryption failed", e);
        }
    }

    public String login(String email, String encryptedPassword) {
        try {
            // Decrypt the password first
            String rawPassword = rsaUtil.decrypt(encryptedPassword);

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                throw new RuntimeException("Invalid email or password");
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }

            // Call external auth service (with resilience)
            externalAuthService.authenticate(email, rawPassword);

            // Generate token and store it
            String token = jwtUtil.generateToken(user.getEmail());
            tokenStore.storeToken(token, user.getEmail());
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Login failed", e);
        }
    }
}
