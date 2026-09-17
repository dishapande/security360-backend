package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_checkpoints")
public class QrCheckpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String checkpointCode;

    @Column(nullable = false)
    private String checkpointName;

    private Long siteId;

    private String location;

    private String qrCode;

    private String nfcCode;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime lastScannedAt;

    public QrCheckpoint() {
    }

    public Long getId() {
        return id;
    }

    public String getCheckpointCode() {
        return checkpointCode;
    }

    public void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }

    public String getCheckpointName() {
        return checkpointName;
    }

    public void setCheckpointName(String checkpointName) {
        this.checkpointName = checkpointName;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getNfcCode() {
        return nfcCode;
    }

    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
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

    public LocalDateTime getLastScannedAt() {
        return lastScannedAt;
    }

    public void setLastScannedAt(LocalDateTime lastScannedAt) {
        this.lastScannedAt = lastScannedAt;
    }
}