package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime punchInTime;

    private LocalDateTime punchOutTime;

    @Column(nullable = false)
    private String punchMethod; // Face, QR, Geo Fence, Manual

    @Column(nullable = false)
    private String status; // Active, Late, Absent, On Duty

    private String shiftType; // Day, Night

    private Double geoLatitude;
    
    private Double geoLongitude;

    private LocalDateTime createdAt;

    // Constructors
    public AttendanceRecord() {}

    public AttendanceRecord(Employee employee, LocalDateTime punchInTime, String punchMethod, String status) {
        this.employee = employee;
        this.punchInTime = punchInTime;
        this.punchMethod = punchMethod;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDateTime getPunchInTime() { return punchInTime; }
    public void setPunchInTime(LocalDateTime punchInTime) { this.punchInTime = punchInTime; }

    public LocalDateTime getPunchOutTime() { return punchOutTime; }
    public void setPunchOutTime(LocalDateTime punchOutTime) { this.punchOutTime = punchOutTime; }

    public String getPunchMethod() { return punchMethod; }
    public void setPunchMethod(String punchMethod) { this.punchMethod = punchMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }

    public Double getGeoLatitude() { return geoLatitude; }
    public void setGeoLatitude(Double geoLatitude) { this.geoLatitude = geoLatitude; }

    public Double getGeoLongitude() { return geoLongitude; }
    public void setGeoLongitude(Double geoLongitude) { this.geoLongitude = geoLongitude; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}