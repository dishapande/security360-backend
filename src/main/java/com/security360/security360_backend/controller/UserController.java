package com.security360.security360_backend.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security360.security360_backend.dto.UserProfileResponse;
import com.security360.security360_backend.entity.User;
import com.security360.security360_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public UserProfileResponse profile(Principal principal) {

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole());
    }

}