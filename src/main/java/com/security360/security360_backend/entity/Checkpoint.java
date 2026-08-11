package com.security360.security360_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "checkpoints")
public class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patrol_route_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PatrolRoute patrolRoute;

    @Column(nullable = false)
    private String checkpointName;

    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    private Double latitude;
    private Double longitude;

    @Column(name = "qr_code_hash", unique = true)
    private String qrCodeHash;

    // Constructors
    public Checkpoint() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PatrolRoute getPatrolRoute() { return patrolRoute; }
    public void setPatrolRoute(PatrolRoute patrolRoute) { this.patrolRoute = patrolRoute; }
    public String getCheckpointName() { return checkpointName; }
    public void setCheckpointName(String checkpointName) { this.checkpointName = checkpointName; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getQrCodeHash() { return qrCodeHash; }
    public void setQrCodeHash(String qrCodeHash) { this.qrCodeHash = qrCodeHash; }
}