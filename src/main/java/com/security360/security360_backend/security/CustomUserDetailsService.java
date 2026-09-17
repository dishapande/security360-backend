package com.security360.security360_backend.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.security360.security360_backend.repository.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(
            UserRepository repository) {

        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        return repository.findByEmail(
                email.trim().toLowerCase()
        ).orElseThrow(() ->
                new UsernameNotFoundException(
                        "User Not Found"
                )
        );
    }
}