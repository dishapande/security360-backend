package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.PsaraCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsaraComplianceRepository
        extends JpaRepository<PsaraCompliance, Long> {
}