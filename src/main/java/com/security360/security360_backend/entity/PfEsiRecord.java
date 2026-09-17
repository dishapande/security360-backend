package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pf_esi_records")
public class PfEsiRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String employeeCode;

    @Column(length = 150)
    private String employeeName;

    @Column(length = 50)
    private String uan;

    @Column(length = 50)
    private String esicNumber;

    @Column(length = 30)
    private String month;

    @Column(precision = 15, scale = 2)
    private BigDecimal employeeContribution = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal employerContribution = BigDecimal.ZERO;

    /*
     * EMPLOYEE
     * EMPLOYER
     * MONTHLY_RETURN
     * PF_CHALLAN
     * ESI_CHALLAN
     * HISTORY
     * COMPLIANCE
     * REPORT
     */
    @Column(nullable = false, length = 30)
    private String type;

    /*
     * PENDING
     * FILED
     * PAID
     * COMPLETED
     * EXPIRED
     */
    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 100)
    private String challanNumber;

    @Column(length = 100)
    private String returnNumber;

    private LocalDate dueDate;

    private LocalDate filingDate;

    @Column(length = 500)
    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (employeeContribution == null) {
            employeeContribution = BigDecimal.ZERO;
        }

        if (employerContribution == null) {
            employerContribution = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public PfEsiRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUan() {
        return uan;
    }

    public void setUan(String uan) {
        this.uan = uan;
    }

    public String getEsicNumber() {
        return esicNumber;
    }

    public void setEsicNumber(String esicNumber) {
        this.esicNumber = esicNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(BigDecimal employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}