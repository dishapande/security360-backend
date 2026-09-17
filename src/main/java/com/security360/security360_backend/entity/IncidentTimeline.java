package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_timeline")
public class IncidentTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @Column(nullable = false)
    private String action; // Created, Assigned, Updated, Escalated, Resolved, Closed

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Incident getIncident() { return incident; }
    public void setIncident(Incident incident) { this.incident = incident; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}