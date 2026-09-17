package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByOrderByIdDesc();

    List<LeaveRequest> findByStatusOrderByIdDesc(String status);

    List<LeaveRequest> findByEmployeeIdOrderByIdDesc(String employeeId);
}