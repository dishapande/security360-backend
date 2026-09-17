package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cameras")
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "camera_id", nullable = false, unique = true, length = 30)
    private String cameraId;

    @Column(name = "camera_name", nullable = false, length = 150)
    private String cameraName;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(nullable = false, length = 30)
    private String protocol;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, length = 30)
    private String recording;

    @Column(name = "ai_detection", nullable = false, length = 30)
    private String aiDetection;

    @Column(nullable = false)
    private Integer alerts = 0;

    @Column(name = "last_snapshot")
    private LocalDateTime lastSnapshot;

    @Lob
    @Column(name = "snapshot", columnDefinition = "LONGTEXT")
    private String snapshot;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (recording == null || recording.isBlank()) {
            recording = "Stopped";
        }

        if (aiDetection == null || aiDetection.isBlank()) {
            aiDetection = "Disabled";
        }

        if (alerts == null) {
            alerts = 0;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }

    public String getAiDetection() {
        return aiDetection;
    }

    public void setAiDetection(String aiDetection) {
        this.aiDetection = aiDetection;
    }

    public Integer getAlerts() {
        return alerts;
    }

    public void setAlerts(Integer alerts) {
        this.alerts = alerts;
    }

    public LocalDateTime getLastSnapshot() {
        return lastSnapshot;
    }

    public void setLastSnapshot(LocalDateTime lastSnapshot) {
        this.lastSnapshot = lastSnapshot;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}