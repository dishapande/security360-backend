package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllByOrderByCreatedAtDesc();
    List<Contract> findByEndDateBetween(LocalDate start, LocalDate end);
    long countByStatus(String status);
}