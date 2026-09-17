package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expiry_alerts")
public class ExpiryAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 150)
    private String reference;

    @Column(length = 150)
    private String owner;

    @Column(length = 150)
    private String site;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, length = 30)
    private String renewalStatus = "PENDING";

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 255)
    private String fileName;

    @Column(length = 150)
    private String fileContentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] fileData;

    public ExpiryAlert() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (renewalStatus == null || renewalStatus.isBlank()) {
            renewalStatus = "PENDING";
        }

        updateStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updateStatus();
    }

    public void updateStatus() {
        if (expiryDate == null) {
            status = "UPCOMING";
            return;
        }

        LocalDate today = LocalDate.now();

        if (expiryDate.isBefore(today)) {
            status = "EXPIRED";
        } else if (!expiryDate.isAfter(today.plusDays(7))) {
            status = "CRITICAL";
        } else if (!expiryDate.isAfter(today.plusDays(30))) {
            status = "UPCOMING";
        } else {
            status = "VALID";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        updateStatus();
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getStatus() {
        updateStatus();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}