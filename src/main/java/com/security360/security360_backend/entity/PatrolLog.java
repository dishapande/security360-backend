package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patrol_logs")
public class PatrolLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patrol_session_id", nullable = false)
    private PatrolSession patrolSession;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checkpoint_id", nullable = false)
    private Checkpoint checkpoint;

    @Column(name = "scan_time", nullable = false)
    private LocalDateTime scanTime;

    @Column(name = "scan_method", nullable = false)
    private String scanMethod; // 'QR', 'NFC', 'Manual'

    @Column(name = "photo_url")
    private String photoUrl; // Stores path to saved photo

    @Column(name = "guard_notes", columnDefinition = "TEXT")
    private String guardNotes;

    @Column(name = "late_seconds")
    private Integer lateSeconds = 0;

    // Constructors
    public PatrolLog() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PatrolSession getPatrolSession() { return patrolSession; }
    public void setPatrolSession(PatrolSession patrolSession) { this.patrolSession = patrolSession; }
    public Checkpoint getCheckpoint() { return checkpoint; }
    public void setCheckpoint(Checkpoint checkpoint) { this.checkpoint = checkpoint; }
    public LocalDateTime getScanTime() { return scanTime; }
    public void setScanTime(LocalDateTime scanTime) { this.scanTime = scanTime; }
    public String getScanMethod() { return scanMethod; }
    public void setScanMethod(String scanMethod) { this.scanMethod = scanMethod; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getGuardNotes() { return guardNotes; }
    public void setGuardNotes(String guardNotes) { this.guardNotes = guardNotes; }
    public Integer getLateSeconds() { return lateSeconds; }
    public void setLateSeconds(Integer lateSeconds) { this.lateSeconds = lateSeconds; }
}