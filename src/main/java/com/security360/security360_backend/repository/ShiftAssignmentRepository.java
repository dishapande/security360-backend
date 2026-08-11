package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {
    List<ShiftAssignment> findByAssignmentDate(LocalDate date);
    List<ShiftAssignment> findByEmployeeId(Long employeeId);
}