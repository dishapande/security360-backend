package com.security360.security360_backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // =====================================================
    // SIGNING KEY
    // =====================================================
    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // =====================================================
    // GENERATE JWT
    // =====================================================
    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // =====================================================
    // EXTRACT EMAIL
    // =====================================================
    public String extractEmail(String token) {

        return extractClaims(token)
                .getSubject();
    }

    // =====================================================
    // VALIDATE JWT
    // =====================================================
    public boolean isTokenValid(
            String token,
            String email) {

        try {

            Claims claims =
                    extractClaims(token);

            String tokenEmail =
                    claims.getSubject();

            Date expirationDate =
                    claims.getExpiration();

            return tokenEmail != null
                    && tokenEmail.equalsIgnoreCase(email)
                    && expirationDate != null
                    && expirationDate.after(
                            new Date()
                    );

        } catch (Exception e) {

            return false;
        }
    }

    // =====================================================
    // EXTRACT CLAIMS
    // =====================================================
    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}