package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByOrderByCreatedAtDesc();
}