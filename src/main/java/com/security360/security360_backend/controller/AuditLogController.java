package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.AuditLog;
import com.security360.security360_backend.service.AuditLogService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = {
        "http://localhost:8080",
        "http://localhost:8081"
})
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(
            AuditLogService auditLogService
    ) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getLogs() {

        List<AuditLogResponse> response =
                auditLogService.getAllLogs()
                        .stream()
                        .map(AuditLogResponse::new)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {

        return ResponseEntity.ok(
                auditLogService.getStats()
        );
    }

    @PostMapping
    public ResponseEntity<AuditLogResponse> createLog(
            @RequestBody CreateAuditLogRequest request,
            HttpServletRequest httpRequest,
            Authentication authentication
    ) {

        String username =
                request.username();

        if ((username == null || username.isBlank())
                && authentication != null) {

            username = authentication.getName();
        }

        AuditLog saved =
                auditLogService.createFromRequest(
                        httpRequest,
                        username,
                        request.action(),
                        request.target(),
                        request.userRole(),
                        request.status()
                );

        return ResponseEntity.ok(
                new AuditLogResponse(saved)
        );
    }

    public record CreateAuditLogRequest(
            String username,
            String action,
            String target,
            String userRole,
            String status
    ) {
    }

    public record AuditLogResponse(
            Long id,
            String username,
            String action,
            String target,
            String ipAddress,
            String browser,
            String userRole,
            String systemName,
            String status,
            String createdAt
    ) {

        public AuditLogResponse(AuditLog log) {

            this(
                    log.getId(),
                    log.getUsername(),
                    log.getAction(),
                    log.getTarget(),
                    log.getIpAddress(),
                    log.getBrowser(),
                    log.getUserRole(),
                    log.getSystemName(),
                    log.getStatus(),
                    log.getCreatedAt() != null
                            ? log.getCreatedAt().toString()
                            : null
            );
        }
    }
}