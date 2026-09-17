package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_fuel")
public class FuelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "fuel_date")
    private LocalDate fuelDate;

    @Column(name = "fuel_type")
    private String fuelType;

    private BigDecimal quantity;

    private BigDecimal amount;

    private BigDecimal odometer;

    private String station;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public FuelRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getFuelDate() {
        return fuelDate;
    }

    public void setFuelDate(LocalDate fuelDate) {
        this.fuelDate = fuelDate;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOdometer() {
        return odometer;
    }

    public void setOdometer(BigDecimal odometer) {
        this.odometer = odometer;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}