package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    // Fetch all access logs sorted by newest first
    List<AccessLog> findAllByOrderByAccessTimeDesc();
}