package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "door_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Door door;

    @Column(name = "access_method", nullable = false)
    private String accessMethod; // 'RFID', 'Finger', 'Card', 'Manual'

    @Column(nullable = false)
    private boolean granted; // TRUE = Allowed, FALSE = Denied

    private String reason; // e.g., 'Access denied - No permission'

    @Column(name = "access_time")
    private LocalDateTime accessTime;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.accessTime = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Door getDoor() { return door; }
    public void setDoor(Door door) { this.door = door; }

    public String getAccessMethod() { return accessMethod; }
    public void setAccessMethod(String accessMethod) { this.accessMethod = accessMethod; }

    // 🟢 Standard getter for boolean
    public boolean isGranted() { return granted; }

    // 🟢 NEW: Add this manual getter for the Controller
    public boolean getGranted() { return granted; }

    public void setGranted(boolean granted) { this.granted = granted; }

    // 🟢 ADD THESE METHODS! They are missing and will crash the Controller.
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getAccessTime() { return accessTime; }
    public void setAccessTime(LocalDateTime accessTime) { this.accessTime = accessTime; }
}