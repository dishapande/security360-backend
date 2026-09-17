package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    
    // Find all attendance records for a specific employee
    List<AttendanceRecord> findByEmployeeId(Long employeeId);

    // Find attendance records between two dates (for Reports)
    List<AttendanceRecord> findByPunchInTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Count how many specific statuses exist today (for Dashboard KPIs)
    long countByStatusAndPunchInTimeBetween(String status, LocalDateTime start, LocalDateTime end);

     List<AttendanceRecord> findByEmployeeIdAndPunchInTimeBetween(
        Long employeeId, 
        LocalDateTime start, 
        LocalDateTime end
    );
}