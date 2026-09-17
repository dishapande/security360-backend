package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "police_verifications")
public class PoliceVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus = "Pending";

    @Column(name = "verification_date")
    private LocalDate verificationDate;

    @Column(name = "police_station")
    private String policeStation;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_expiry")
    private LocalDate documentExpiry;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "document_file_name")
    private String documentFileName;

    @Column(name = "document_content_type")
    private String documentContentType;

    @Lob
    @Column(name = "document_data", columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public PoliceVerification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDate getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDate verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public LocalDate getDocumentExpiry() {
        return documentExpiry;
    }

    public void setDocumentExpiry(LocalDate documentExpiry) {
        this.documentExpiry = documentExpiry;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
