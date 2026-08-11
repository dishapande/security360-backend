package com.security360.security360_backend.dto;

import java.time.LocalDateTime;

public class GuardTrackingResponseDTO {
    private Long employeeId;
    private String fullName;
    private String empCode;
    private String role;
    private String status;
    private double latitude;
    private double longitude;
    private double speedKmh;
    private int batteryPercent;
    private String networkType;
    private String checkpoint;
    private LocalDateTime lastSeen;

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmpCode() { return empCode; }
    public void setEmpCode(String empCode) { this.empCode = empCode; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
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
    public String getCheckpoint() { return checkpoint; }
    public void setCheckpoint(String checkpoint) { this.checkpoint = checkpoint; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
}