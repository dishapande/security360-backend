package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();

    long countByCreatedAtAfter(LocalDateTime dateTime);

    long countByActionIgnoreCase(String action);

    long countByStatusIgnoreCase(String status);

    @Query("""
        SELECT COUNT(DISTINCT a.username)
        FROM AuditLog a
        WHERE a.username IS NOT NULL
        AND a.username <> ''
    """)
    long countDistinctUsers();

    @Query("""
        SELECT COUNT(DISTINCT a.username)
        FROM AuditLog a
        WHERE a.userRole IS NOT NULL
        AND LOWER(a.userRole) LIKE '%admin%'
    """)
    long countDistinctAdmins();

    @Query("""
        SELECT COUNT(DISTINCT a.systemName)
        FROM AuditLog a
        WHERE a.systemName IS NOT NULL
        AND a.systemName <> ''
    """)
    long countDistinctSystems();

    @Query("""
        SELECT COUNT(a)
        FROM AuditLog a
        WHERE LOWER(a.action) LIKE '%login%'
        AND LOWER(a.status) = 'critical'
    """)
    long countFailedLogins();

    @Query("""
        SELECT COUNT(a)
        FROM AuditLog a
        WHERE LOWER(a.status) = 'warning'
        OR LOWER(a.status) = 'critical'
    """)
    long countAnomalies();
}