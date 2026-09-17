package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "patrol_routes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PatrolRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String routeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String siteName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "patrolRoute",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnoreProperties("patrolRoute")
    private List<Checkpoint> checkpoints = new ArrayList<>();

    // Constructors
    public PatrolRoute() {
    }

    public PatrolRoute(
            String routeName,
            String description,
            String siteName
    ) {
        this.routeName = routeName;
        this.description = description;
        this.siteName = siteName;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}