package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // ADD THESE TWO LINES:
    long countByStatus(String status);
}