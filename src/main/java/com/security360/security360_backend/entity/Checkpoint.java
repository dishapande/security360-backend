package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkpoints")
public class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkpoint_code", unique = true, nullable = false)
    private String checkpointCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // QR or NFC

    @Column(nullable = false)
    private String site;

    private String location;

    @Column(nullable = false)
    private String status; // Active / Inactive

    @Column(name = "last_scan_status")
    private String lastScanStatus; // Pending / Scanned / Missed

    @Column(name = "last_scan")
    private LocalDateTime lastScan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCheckpointCode() { return checkpointCode; }
    public void setCheckpointCode(String checkpointCode) { this.checkpointCode = checkpointCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastScanStatus() { return lastScanStatus; }
    public void setLastScanStatus(String lastScanStatus) { this.lastScanStatus = lastScanStatus; }

    public LocalDateTime getLastScan() { return lastScan; }
    public void setLastScan(LocalDateTime lastScan) { this.lastScan = lastScan; }
}