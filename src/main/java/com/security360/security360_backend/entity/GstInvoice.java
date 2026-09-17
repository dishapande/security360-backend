package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gst_invoices")
public class GstInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceNo;

    @Column(nullable = false)
    private String party;

    private String type;

    @Column(nullable = false)
    private String gstin;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal taxableAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal cgst = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal sgst = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal igst = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    private LocalDate issuedDate;

    private LocalDate dueDate;

    private String status;

    public GstInvoice() {
    }

    public Long getId() {
        return id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getCgst() {
        return cgst;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getSgst() {
        return sgst;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getIgst() {
        return igst;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}