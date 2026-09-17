package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "housekeeping_staff")
public class HousekeepingStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @Transient
    private Long staffId; // 🟢 Accepts raw ID from the form

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // Supervisor, Cleaner, Housekeeping Head

    private String phone;
    private String email;

    @Column(nullable = false)
    private String status; // Active, On Leave, Inactive

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Active";
        }
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
        public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}