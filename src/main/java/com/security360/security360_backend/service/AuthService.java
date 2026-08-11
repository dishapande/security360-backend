package com.security360.security360_backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security360.security360_backend.dto.AuthResponse;
import com.security360.security360_backend.dto.LoginRequest;
import com.security360.security360_backend.dto.RegisterRequest;
import com.security360.security360_backend.entity.User;
import com.security360.security360_backend.enums.Role;
import com.security360.security360_backend.repository.UserRepository;
import com.security360.security360_backend.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, "Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🟢 Default role + Handle role from request (if present)
        Role roleToAssign = Role.GUARD;
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                roleToAssign = Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                // If invalid role is sent, fallback to Guard
                roleToAssign = Role.GUARD;
            }
        }
        user.setRole(roleToAssign);

        userRepository.save(user);

        return new AuthResponse(null, "Registration Successful");
    }
    
    public AuthResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 2. Debug logs (Optional)
        System.out.println("==================================");
        System.out.println("Email            : " + request.getEmail());
        System.out.println("Request Password : [" + request.getPassword() + "]");
        System.out.println("Stored Hash      : " + user.getPassword());

        // 3. Match the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matched = encoder.matches(request.getPassword(), user.getPassword());

        System.out.println("Password Matched : " + matched);
        System.out.println("==================================");

        if (!matched) {
            throw new RuntimeException("Invalid credentials");
        }

        // 4. Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        // 5. Return token, email, and role
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}