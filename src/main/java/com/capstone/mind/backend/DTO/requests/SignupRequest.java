package com.capstone.mind.backend.DTO.requests;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*

public class SignupRequest {
    @NotBlank(message = "Name is required")
    private String fname;

    @Nullable
    private String lname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Nullable
    public String getLname() {
        return lname;
    }


    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password;
    }


}*/



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "First name is required")
    @JsonProperty("fname")
    private String fname;

    @JsonProperty("lname")
    private String lname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

    // Getters & setters
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
