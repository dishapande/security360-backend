package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patrol_sessions")
public class PatrolSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patrol_route_id", nullable = false)
    private PatrolRoute patrolRoute;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String status; // 'In Progress', 'Completed', 'Missed', 'Late'

    @Column(name = "supervisor_notes", columnDefinition = "TEXT")
    private String supervisorNotes;

    @Column(name = "supervisor_reviewed")
    private Boolean supervisorReviewed = false;

    // Constructors
    public PatrolSession() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public PatrolRoute getPatrolRoute() { return patrolRoute; }
    public void setPatrolRoute(PatrolRoute patrolRoute) { this.patrolRoute = patrolRoute; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSupervisorNotes() { return supervisorNotes; }
    public void setSupervisorNotes(String supervisorNotes) { this.supervisorNotes = supervisorNotes; }
    public Boolean getSupervisorReviewed() { return supervisorReviewed; }
    public void setSupervisorReviewed(Boolean supervisorReviewed) { this.supervisorReviewed = supervisorReviewed; }
}