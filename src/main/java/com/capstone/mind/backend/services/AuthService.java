/*package com.capstone.mind.backend.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.mind.backend.DTO.data.AuthResponseData;
import com.capstone.mind.backend.DTO.responses.ApiResponse;
import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    /////// LOGIN ///////
    public ApiResponse<AuthResponseData> Login(String email, String password) {
        try {
            // check if user exists
            Optional<User> existingUser = userRepo.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                // check if password matches
                if (passwordEncoder.matches(password, user.getPassword())) {
                    AuthResponseData data = new AuthResponseData(user.getEmail(), user.getFname(), user.getLname());
                    System.out.println("Authenticated user with email '%s'".formatted(email));
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Authentication successful".formatted(), data);
                }

            }

            System.out.println("Invalid credentials attempted using email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid credentials".formatted());

        } catch (

                Exception error) {
            // handle errors
            System.err.println("Error authenticating email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error authenticating");
        }
    }

    /////// SIGNUP ///////
    public ApiResponse<AuthResponseData> Signup(String email, String fname, String lname, String password) {
        try {
            // check if user with email already exists
            Optional<User> existingUser = userRepo.findByEmail(email);
            if (existingUser.isPresent()) {
                System.out.println("User already exists with email '%s'".formatted(email));
                return new ApiResponse<>(HttpStatus.BAD_REQUEST, "User already exists with email '%s'".formatted(email));
            }

            // hash password
            String hashedPassword = passwordEncoder.encode(password);
            User newUser = new User(fname, lname, email, hashedPassword);
            userRepo.save(newUser);

            AuthResponseData data = new AuthResponseData(newUser.getEmail(), newUser.getFname(), newUser.getLname());
            System.out.println("User created with email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.OK, "User created succesfully", data);
        } catch (Exception error) {
            // handle errors
            System.err.println("Error creating new user");
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating new user");
        }
    }

}*/

package com.capstone.mind.backend.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.mind.backend.DTO.data.AuthResponseData;
import com.capstone.mind.backend.DTO.responses.ApiResponse;
import com.capstone.mind.backend.models.User;
import com.capstone.mind.backend.repositories.UserRepository;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final JwtService jwtService; // NEW

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepo, JwtService jwtService) { // NEW
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtService = jwtService; // NEW
    }

    /////// LOGIN ///////
    public ApiResponse<AuthResponseData> Login(String email, String password) {
        try {
            Optional<User> existingUser = userRepo.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();

                if (passwordEncoder.matches(password, user.getPassword())) {
                    // ✅ generate JWT here
                    String token = jwtService.generateToken(user);

                    AuthResponseData data = new AuthResponseData(
                            user.getEmail(),
                            user.getFname(),
                            user.getLname(),
                            token // include token in response
                    );

                    System.out.println("Authenticated user with email '%s'".formatted(email));
                    // FIX: success should be 200 OK (was BAD_REQUEST before)
                    return new ApiResponse<>(HttpStatus.OK, "Authentication successful", data);
                }
            }

            System.out.println("Invalid credentials attempted using email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid credentials");

        } catch (Exception error) {
            System.err.println("Error authenticating email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error authenticating");
        }
    }

    /////// SIGNUP ///////
    public ApiResponse<AuthResponseData> Signup(String email, String fname, String lname, String password) {
        try {
            Optional<User> existingUser = userRepo.findByEmail(email);
            if (existingUser.isPresent()) {
                System.out.println("User already exists with email '%s'".formatted(email));
                return new ApiResponse<>(HttpStatus.BAD_REQUEST, "User already exists with email '%s'".formatted(email));
            }

            String hashedPassword = passwordEncoder.encode(password);
            User newUser = new User(fname, lname, email, hashedPassword);
            userRepo.save(newUser);

            // Optionally, auto-login after signup by issuing a token
            String token = jwtService.generateToken(newUser);

            AuthResponseData data = new AuthResponseData(newUser.getEmail(), newUser.getFname(), newUser.getLname(), token);
            System.out.println("User created with email '%s'".formatted(email));
            return new ApiResponse<>(HttpStatus.OK, "User created successfully", data);

        } catch (Exception error) {
            System.err.println("Error creating new user");
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating new user");
        }
    }
}
