package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sos_incidents")
public class SOSIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String status; // Active, Assigned, Responding, Resolved

    private String assignedTeam;
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Column(name = "resolved_time")
    private LocalDateTime resolvedTime;

    // --- Constructors, Getters and Setters ---
    public SOSIncident() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedTeam() { return assignedTeam; }
    public void setAssignedTeam(String assignedTeam) { this.assignedTeam = assignedTeam; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResponseTime() { return responseTime; }
    public void setResponseTime(LocalDateTime responseTime) { this.responseTime = responseTime; }

    public LocalDateTime getResolvedTime() { return resolvedTime; }
    public void setResolvedTime(LocalDateTime resolvedTime) { this.resolvedTime = resolvedTime; }
}