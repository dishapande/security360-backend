package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "labour_licenses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "license_number")
    }
)
public class LabourLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String state;

    private String authority;

    private String owner;

    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    private LocalDate renewalDate;

    private LocalDate inspectionDate;

    private String inspectionStatus;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String documentName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public LabourLicense() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Active";
        }

        if (this.inspectionStatus == null || this.inspectionStatus.trim().isEmpty()) {
            this.inspectionStatus = "Pending";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}