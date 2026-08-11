package com.security360.security360_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.security360.security360_backend.dto.AuthResponse;
import com.security360.security360_backend.dto.LoginRequest;
import com.security360.security360_backend.dto.RegisterRequest;
import com.security360.security360_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8081") // 🟢 FIXED: Should point to React port 8081, not 8084!
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("LOGIN API HIT");
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 🟢 Return a 400 Bad Request instead of a 500 Internal Server Error
            return ResponseEntity
                    .status(400)
                    .body(new AuthResponse(null, e.getMessage()));
        }
    }
}