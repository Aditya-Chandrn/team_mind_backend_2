package com.capstone.mind.backend.services;

import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String firstName, String lastName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Hash password before saving
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setFname(firstName);
        user.setLname(lastName);
        user.setEmail(email);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
}
