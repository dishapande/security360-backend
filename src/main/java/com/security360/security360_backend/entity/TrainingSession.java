package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String sessionCode;

    public String courseCode;
    public String courseName;
    public String date;
    public String time;
    public String trainer;
    public String location;
    public Integer capacity;
    public String status;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    // 🟢 Required by Jackson
    public TrainingSession() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "Upcoming";
        if (this.capacity == null) this.capacity = 0;
    }

    // 🟢 Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTrainer() { return trainer; }
    public void setTrainer(String trainer) { this.trainer = trainer; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}