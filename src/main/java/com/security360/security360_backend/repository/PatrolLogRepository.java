package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.PatrolLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrolLogRepository extends JpaRepository<PatrolLog, Long> {
}