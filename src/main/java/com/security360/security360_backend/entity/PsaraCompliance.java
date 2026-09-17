package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "psara_compliance")
public class PsaraCompliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String licenseNumber;
    private String state;
    private String owner;
    private String type;
    private String issuingAuthority;

    private LocalDate issueDate;
    private LocalDate expiryDate;
    private LocalDate renewalDate;

    private String status;

    @Column(length = 2000)
    private String notes;

    private String certificateFileName;
    private String certificateFilePath;

    public PsaraCompliance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
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

    public String getCertificateFileName() {
        return certificateFileName;
    }

    public void setCertificateFileName(String certificateFileName) {
        this.certificateFileName = certificateFileName;
    }

    public String getCertificateFilePath() {
        return certificateFilePath;
    }

    public void setCertificateFilePath(String certificateFilePath) {
        this.certificateFilePath = certificateFilePath;
    }
}