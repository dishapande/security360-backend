package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkpoint_scans")
public class CheckpointScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checkpoint_id", nullable = false)
    private Checkpoint checkpoint;

    @Column(nullable = false)
    private LocalDateTime scanTime;

    private String status;

    public CheckpointScan() {
    }

    public Long getId() {
        return id;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}