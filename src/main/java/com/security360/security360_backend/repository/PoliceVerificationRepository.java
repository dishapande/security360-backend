package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.PoliceVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoliceVerificationRepository
        extends JpaRepository<PoliceVerification, Long> {

    List<PoliceVerification> findByEmployeeNameContainingIgnoreCase(
            String employeeName
    );

    List<PoliceVerification> findByEmployeeIdContainingIgnoreCase(
            String employeeId
    );

    List<PoliceVerification> findByVerificationStatusIgnoreCase(
            String verificationStatus
    );
}
