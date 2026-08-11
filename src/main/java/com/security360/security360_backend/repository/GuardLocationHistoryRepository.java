package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.GuardLocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GuardLocationHistoryRepository extends JpaRepository<GuardLocationHistory, Long> {
    List<GuardLocationHistory> findByEmployeeIdAndTimestampAfter(Long employeeId, LocalDateTime timestamp);
}