package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gst_returns")
public class GstReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String returnId;

    @Column(nullable = false)
    private String returnType;

    @Column(nullable = false)
    private String period;

    private LocalDate dueDate;

    private LocalDate filedDate;

    private String status;

    @Column(precision = 15, scale = 2)
    private BigDecimal taxPayable = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal itc = BigDecimal.ZERO;

    public GstReturn() {
    }

    public Long getId() {
        return id;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getFiledDate() {
        return filedDate;
    }

    public void setFiledDate(LocalDate filedDate) {
        this.filedDate = filedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTaxPayable() {
        return taxPayable;
    }

    public void setTaxPayable(BigDecimal taxPayable) {
        this.taxPayable = taxPayable;
    }

    public BigDecimal getItc() {
        return itc;
    }

    public void setItc(BigDecimal itc) {
        this.itc = itc;
    }
}