package com.capstone.mind.backend.DTO.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

    public String getPassword() {
        return password;
    }



    public String getEmail() {
        return email;
    }


}