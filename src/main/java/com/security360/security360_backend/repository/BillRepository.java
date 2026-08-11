package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Fetch all bills sorted by newest first (for the table)
    List<Bill> findAllByOrderByCreatedAtDesc();

    // Count bills by status (for Dashboard stats)
    long countByStatus(String status);
}