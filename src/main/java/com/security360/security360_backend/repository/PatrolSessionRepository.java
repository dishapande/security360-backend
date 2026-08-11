package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.PatrolSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatrolSessionRepository extends JpaRepository<PatrolSession, Long> {
    
    // Find the currently active patrol for a specific guard
    Optional<PatrolSession> findByEmployeeIdAndStatus(Long employeeId, String status);
    
    // 🟢 THIS WAS MISSING! It fetches all patrol sessions sorted by newest first.
    List<PatrolSession> findAllByOrderByStartTimeDesc();
}