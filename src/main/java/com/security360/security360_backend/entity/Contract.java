package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Client client;

    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    private String title;
    private String contractType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String terms;

    private Double pricing;

    private String status;

    @Column(name = "agreement_file_url")
    private String agreementFileUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Active";
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
    public Double getPricing() { return pricing; }
    public void setPricing(Double pricing) { this.pricing = pricing; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAgreementFileUrl() { return agreementFileUrl; }
    public void setAgreementFileUrl(String agreementFileUrl) { this.agreementFileUrl = agreementFileUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}