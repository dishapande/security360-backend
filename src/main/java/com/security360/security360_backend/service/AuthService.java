package com.security360.security360_backend.service;

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

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =====================================================
    // REGISTER
    // =====================================================
    public AuthResponse register(RegisterRequest request) {

        if (request == null) {
            return new AuthResponse(
                    null,
                    "Invalid registration request"
            );
        }

        if (request.getFullName() == null ||
                request.getFullName().trim().isEmpty()) {

            return new AuthResponse(
                    null,
                    "Full name is required"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            return new AuthResponse(
                    null,
                    "Email is required"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            return new AuthResponse(
                    null,
                    "Password is required"
            );
        }

        String fullName = request.getFullName().trim();
        String email = request.getEmail().trim().toLowerCase();

        // Check existing email
        if (userRepository.existsByEmail(email)) {
            return new AuthResponse(
                    null,
                    "Email already exists"
            );
        }

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);

        // Never store plain-text password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        /*
         * IMPORTANT SECURITY RULE:
         *
         * Public registration can NEVER choose its role.
         *
         * Every newly registered account is GUARD.
         *
         * Even if someone sends:
         * "role": "ADMIN"
         *
         * it will be ignored because RegisterRequest
         * does not contain a role field.
         */
        user.setRole(Role.GUARD);

        // Save user
        userRepository.save(user);

        System.out.println("==================================");
        System.out.println("USER REGISTERED");
        System.out.println("Email : " + user.getEmail());
        System.out.println("Role  : " + user.getRole());
        System.out.println("==================================");

        return new AuthResponse(
                null,
                "Registration Successful"
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================
    public AuthResponse login(LoginRequest request) {

        if (request == null ||
                request.getEmail() == null ||
                request.getPassword() == null) {

            throw new RuntimeException("Invalid credentials");
        }

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        System.out.println("==================================");
        System.out.println("LOGIN ATTEMPT");
        System.out.println("Email : " + email);
        System.out.println("==================================");

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials")
                );

        // Compare password with encrypted password
        boolean passwordMatched = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        System.out.println(
                "Password matched : " + passwordMatched
        );

        if (!passwordMatched) {

            System.out.println(
                    "LOGIN FAILED: Invalid credentials"
            );

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        // Generate JWT
        String token = jwtService.generateToken(
                user.getEmail()
        );

        System.out.println("LOGIN SUCCESSFUL");
        System.out.println("Email : " + user.getEmail());
        System.out.println("Role  : " + user.getRole());
        System.out.println("==================================");

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole()
        );
    }
}