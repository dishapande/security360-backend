package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.GuardLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardLocationRepository extends JpaRepository<GuardLocation, Long> {
    GuardLocation findByEmployeeId(Long employeeId);
}