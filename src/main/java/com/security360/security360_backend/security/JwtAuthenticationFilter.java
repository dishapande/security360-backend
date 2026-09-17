package com.security360.security360_backend.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("===== JWT FILTER =====");
        System.out.println(
                "Request URI: " +
                request.getRequestURI()
        );

        // Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        // Never print the actual JWT token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "No Bearer Token Found"
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        System.out.println(
                "Authorization header received"
        );

        // Extract token
        String token =
                authHeader.substring(7);

        try {

            // Extract email from JWT
            String email =
                    jwtService.extractEmail(token);

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // Load user from database
                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                // Validate token
                if (jwtService.isTokenValid(
                        token,
                        userDetails.getUsername()
                )) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );

                    System.out.println(
                            "JWT authentication successful for: "
                                    + email
                    );

                } else {

                    System.out.println(
                            "Invalid or expired JWT"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Authentication Failed"
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}