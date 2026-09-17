package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", unique = true)
    private String assetCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(name = "issued_to")
    private String issuedTo;

    private String site;

    @Column(name = "next_service")
    private String nextService;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "Available";
        if (this.issuedTo == null) this.issuedTo = "—";
        if (this.nextService == null) this.nextService = "—";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getIssuedTo() { return issuedTo; }
    public void setIssuedTo(String issuedTo) { this.issuedTo = issuedTo; }
    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }
    public String getNextService() { return nextService; }
    public void setNextService(String nextService) { this.nextService = nextService; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}