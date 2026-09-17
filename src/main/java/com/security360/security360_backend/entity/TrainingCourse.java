package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_courses")
public class TrainingCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String courseCode;

    @Column(nullable = false)
    public String name;

    public Integer hrs;
    public Integer enrolled;
    public String instructor;
    public String category;
    public String status;
    public Integer completion;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    // 🟢 Required by Jackson for JSON deserialization
    public TrainingCourse() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "Pending";
        if (this.enrolled == null) this.enrolled = 0;
        if (this.completion == null) this.completion = 0;
    }

    // 🟢 Getters and Setters (Required by Jackson)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getHrs() { return hrs; }
    public void setHrs(Integer hrs) { this.hrs = hrs; }

    public Integer getEnrolled() { return enrolled; }
    public void setEnrolled(Integer enrolled) { this.enrolled = enrolled; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCompletion() { return completion; }
    public void setCompletion(Integer completion) { this.completion = completion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}