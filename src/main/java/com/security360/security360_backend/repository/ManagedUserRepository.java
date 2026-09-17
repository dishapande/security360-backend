package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ManagedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagedUserRepository
        extends JpaRepository<ManagedUser, Long> {

    Optional<ManagedUser> findByUserCode(String userCode);

    Optional<ManagedUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}