package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "housekeeping_tasks")
public class HousekeepingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "tasks"})
    private HousekeepingStaff staff;

    // 🟢 ADD THIS! This accepts the raw ID from React, but does not save it as a database column.
    @Transient
    private Long staffId;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String area; // Lobby, Office, Parking, etc.

    @Column(nullable = false)
    private String priority; // Low, Medium, High

    @Column(nullable = false)
    private String status; // Pending, In Progress, Completed, Cancelled

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- Pre-persist hook ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Pending"; // 🟢 Default status if React doesn't send one
        }
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HousekeepingStaff getStaff() { return staff; }
    public void setStaff(HousekeepingStaff staff) { this.staff = staff; }
    
    // 🟢 GETTER & SETTER FOR THE TRANSIENT FIELD
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}