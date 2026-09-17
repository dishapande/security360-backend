package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doors")
public class Door {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "door_name", nullable = false)
    private String doorName;

    private String location;

    @Column(nullable = false)
    private String status; // 'Locked', 'Unlocked', 'Maintenance'

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Locked";
        }
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDoorName() { return doorName; }
    public void setDoorName(String doorName) { this.doorName = doorName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}