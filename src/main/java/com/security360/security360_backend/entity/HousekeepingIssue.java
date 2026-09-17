package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "housekeeping_issues")
public class HousekeepingIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional link to a task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private HousekeepingTask task;

    @Column(name = "reported_by")
    private String reportedBy;

    @Column(name = "issue_description", columnDefinition = "TEXT", nullable = false)
    private String issueDescription;

    @Column(nullable = false)
    private String status; // Open, In Progress, Resolved

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Open";
        }
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HousekeepingTask getTask() { return task; }
    public void setTask(HousekeepingTask task) { this.task = task; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}