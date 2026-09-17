package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "client_payments")
public class ClientPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, length = 80)
    private String invoiceNumber;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_amount", precision = 15, scale = 2)
    private BigDecimal outstandingAmount = BigDecimal.ZERO;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(length = 30)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}