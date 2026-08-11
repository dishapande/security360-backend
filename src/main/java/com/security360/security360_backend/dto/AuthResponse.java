package com.security360.security360_backend.dto;

import com.security360.security360_backend.enums.Role;

public class AuthResponse {

    private String token;
    private String email;
    private Role role;
    private String message;

    // 1. Default Constructor
    public AuthResponse() {
    }

    // 2. Constructor for REGISTER (Returning a message)
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // 3. 🟢 NEW Constructor for LOGIN (Returning token + email + role)
    public AuthResponse(String token, String email, Role role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // --- Getters and Setters ---

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}