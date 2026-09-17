package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.SLAContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SLAContractRepository extends JpaRepository<SLAContract, Long> {

    // Fetch all SLAs ordered by creation date (newest first)
    List<SLAContract> findAllByOrderByCreatedAtDesc();
    
    // Used for Dashboard Reports
    long countBySlaNameContaining(String keyword);
}