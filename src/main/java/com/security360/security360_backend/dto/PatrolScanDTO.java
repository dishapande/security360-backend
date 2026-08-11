package com.security360.security360_backend.dto;

public class PatrolScanDTO {

    // --- Fields ---
    private Long patrolSessionId;
    private Long checkpointId;
    private String scanMethod; // QR, NFC, Manual
    private String guardNotes;
    private String photoBase64; // Optional photo upload

    // --- Default Constructor (Required by Spring Boot) ---
    public PatrolScanDTO() {
    }

    // --- Parameterized Constructor (Optional, good for testing) ---
    public PatrolScanDTO(Long patrolSessionId, Long checkpointId, String scanMethod, String guardNotes, String photoBase64) {
        this.patrolSessionId = patrolSessionId;
        this.checkpointId = checkpointId;
        this.scanMethod = scanMethod;
        this.guardNotes = guardNotes;
        this.photoBase64 = photoBase64;
    }

    // --- Getters and Setters ---

    public Long getPatrolSessionId() {
        return patrolSessionId;
    }

    public void setPatrolSessionId(Long patrolSessionId) {
        this.patrolSessionId = patrolSessionId;
    }

    public Long getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Long checkpointId) {
        this.checkpointId = checkpointId;
    }

    public String getScanMethod() {
        return scanMethod;
    }

    public void setScanMethod(String scanMethod) {
        this.scanMethod = scanMethod;
    }

    public String getGuardNotes() {
        return guardNotes;
    }

    public void setGuardNotes(String guardNotes) {
        this.guardNotes = guardNotes;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }
}