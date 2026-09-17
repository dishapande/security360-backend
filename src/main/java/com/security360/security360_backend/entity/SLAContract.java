package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sla_contracts")
public class SLAContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Contract contract;

    @Column(name = "sla_name", nullable = false)
    private String slaName;

    @Column(name = "response_time_minutes", nullable = false)
    private Integer responseTimeMinutes; // e.g., 5 minutes

    @Column(name = "patrols_per_day")
    private Integer patrolsPerDay; // e.g., 4 patrols per day

    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private BigDecimal attendancePercentage; // e.g., 95.00%

    @Column(name = "penalty_amount", precision = 10, scale = 2)
    private BigDecimal penaltyAmount; // e.g., 5000.00

    @Column(name = "client_satisfaction_score")
    private Integer clientSatisfactionScore; // 1 to 10

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }

    public String getSlaName() { return slaName; }
    public void setSlaName(String slaName) { this.slaName = slaName; }

    public Integer getResponseTimeMinutes() { return responseTimeMinutes; }
    public void setResponseTimeMinutes(Integer responseTimeMinutes) { this.responseTimeMinutes = responseTimeMinutes; }

    public Integer getPatrolsPerDay() { return patrolsPerDay; }
    public void setPatrolsPerDay(Integer patrolsPerDay) { this.patrolsPerDay = patrolsPerDay; }

    public BigDecimal getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public Integer getClientSatisfactionScore() { return clientSatisfactionScore; }
    public void setClientSatisfactionScore(Integer clientSatisfactionScore) { this.clientSatisfactionScore = clientSatisfactionScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}