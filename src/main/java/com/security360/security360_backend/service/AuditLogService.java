package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.AuditLog;
import com.security360.security360_backend.repository.AuditLogRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog createLog(
            String username,
            String action,
            String target,
            String ipAddress,
            String browser,
            String userRole,
            String systemName,
            String status
    ) {

        AuditLog log = new AuditLog();

        log.setUsername(username);
        log.setAction(action);
        log.setTarget(target);
        log.setIpAddress(ipAddress);
        log.setBrowser(browser);
        log.setUserRole(userRole);
        log.setSystemName(systemName);
        log.setStatus(status);

        return auditLogRepository.save(log);
    }

    public AuditLog createFromRequest(
            HttpServletRequest request,
            String username,
            String action,
            String target,
            String userRole,
            String status
    ) {

        String ipAddress = getClientIp(request);

        String browser = request.getHeader("User-Agent");

        if (browser == null || browser.isBlank()) {
            browser = "Unknown";
        }

        return createLog(
                username,
                action,
                target,
                ipAddress,
                browser,
                userRole,
                "Security360",
                status
        );
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    public Map<String, Object> getStats() {

        LocalDateTime last24Hours =
                LocalDateTime.now().minusHours(24);

        Map<String, Object> stats = new HashMap<>();

        stats.put(
                "events24h",
                auditLogRepository.countByCreatedAtAfter(last24Hours)
        );

        stats.put(
                "users",
                auditLogRepository.countDistinctUsers()
        );

        stats.put(
                "admins",
                auditLogRepository.countDistinctAdmins()
        );

        stats.put(
                "failedLogins",
                auditLogRepository.countFailedLogins()
        );

        stats.put(
                "systems",
                auditLogRepository.countDistinctSystems()
        );

        stats.put(
                "anomalies",
                auditLogRepository.countAnomalies()
        );

        return stats;
    }

    public void deleteLog(Long id) {
        auditLogRepository.deleteById(id);
    }

    private String getClientIp(HttpServletRequest request) {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        String realIp =
                request.getHeader("X-Real-IP");

        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }

        return request.getRemoteAddr();
    }
}