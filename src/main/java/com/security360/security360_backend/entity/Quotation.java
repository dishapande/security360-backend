package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Client client;

    @Column(name = "quotation_number", nullable = false, unique = true)
    private String quotationNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "tax_percent", precision = 5, scale = 2)
    private BigDecimal taxPercent; // Default is 18.00

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status; // 'Pending', 'Approved', 'Rejected', 'Converted'

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "converted_at")
    private LocalDateTime convertedAt;

    // 🟢 Used to link to an Invoice (Not a database column)
    @Transient
    private Long invoiceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Pending";
        }
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getQuotationNumber() { return quotationNumber; }
    public void setQuotationNumber(String quotationNumber) { this.quotationNumber = quotationNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxPercent() { return taxPercent; }
    public void setTaxPercent(BigDecimal taxPercent) { this.taxPercent = taxPercent; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getConvertedAt() { return convertedAt; }
    public void setConvertedAt(LocalDateTime convertedAt) { this.convertedAt = convertedAt; }

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}