package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 150)
    private String username;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "target", length = 255)
    private String target;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "browser", length = 500)
    private String browser;

    @Column(name = "user_role", length = 100)
    private String userRole;

    @Column(name = "system_name", length = 150)
    private String systemName;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    @PrePersist
    public void beforeCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null || status.isBlank()) {
            status = "Active";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}