package com.security360.security360_backend.dto;

import com.security360.security360_backend.enums.Role;

public class UserProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id,
                               String fullName,
                               String email,
                               Role role) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}