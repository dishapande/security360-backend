package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.LabourLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabourLicenseRepository
        extends JpaRepository<LabourLicense, Long> {

    Optional<LabourLicense> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);
}