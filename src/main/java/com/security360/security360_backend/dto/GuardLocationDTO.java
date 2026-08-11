package com.security360.security360_backend.dto;

public class GuardLocationDTO {
    private Long employeeId;
    private double latitude;
    private double longitude;
    private double speedKmh;
    private int batteryPercent;
    private String networkType;
    private String checkpoint;

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
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
}