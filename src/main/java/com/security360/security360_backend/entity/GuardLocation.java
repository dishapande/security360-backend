package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guard_locations")
public class GuardLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private double latitude;
    private double longitude;
    private double speedKmh;
    private int batteryPercent;
    private String networkType;
    private LocalDateTime lastSeen;
    private String currentCheckpoint;

    // Constructors
    public GuardLocation() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getSpeedKmh() { return speedKmh; }
    public void setSpeedKmh(double speedKmh) { this.speedKmh = speedKmh; }

    public int getBatteryPercent() { return batteryPercent; }
    public void setBatteryPercent(int batteryPercent) { this.batteryPercent = batteryPercent; }

    public String getNetworkType() { return networkType; }
    public void setNetworkType(String networkType) { this.networkType = networkType; }

    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }

    // 🟢 Primary Getter: matches the field name
    public String getCurrentCheckpoint() { return currentCheckpoint; }
    public void setCurrentCheckpoint(String currentCheckpoint) { this.currentCheckpoint = currentCheckpoint; }

    // 🟢 ALIAS Getter: This fixes "undefined method getCheckpoint()" in your Controller
    public String getCheckpoint() {
        return this.currentCheckpoint;
    }

    // 🟢 ALIAS Setter: This matches the setCheckpoint() you already added
    public void setCheckpoint(String checkpoint) {
        this.currentCheckpoint = checkpoint;
    }
}