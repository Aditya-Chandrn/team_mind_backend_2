package com.capstone.mind.backend.DTO.data;

public class AuthResponseData {
    private String email;
    private String fname;
    private String lname;
    private String token;

    public AuthResponseData() {}

    public AuthResponseData(String email, String fname, String lname) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
    }

    public AuthResponseData(String email, String fname, String lname, String token) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.token = token;
    }

    public String getEmail() { return email; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getToken() { return token; }

    public void setEmail(String email) { this.email = email; }
    public void setFname(String fname) { this.fname = fname; }
    public void setLname(String lname) { this.lname = lname; }
    public void setToken(String token) { this.token = token; }
}
