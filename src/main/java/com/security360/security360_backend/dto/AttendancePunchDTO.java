package com.security360.security360_backend.dto;

public class AttendancePunchDTO {
    private Long employeeId;
    private String punchMethod; // Face, QR, Geo Fence, Manual
    private Double geoLatitude;
    private Double geoLongitude;
    private String qrToken; // 🟢 THIS LINE WAS MISSING

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    
    public String getPunchMethod() { return punchMethod; }
    public void setPunchMethod(String punchMethod) { this.punchMethod = punchMethod; }
    
    public Double getGeoLatitude() { return geoLatitude; }
    public void setGeoLatitude(Double geoLatitude) { this.geoLatitude = geoLatitude; }
    
    public Double getGeoLongitude() { return geoLongitude; }
    public void setGeoLongitude(Double geoLongitude) { this.geoLongitude = geoLongitude; }
    
    // 🟢 THESE GETTER/SETTER WERE MISSING
    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
}