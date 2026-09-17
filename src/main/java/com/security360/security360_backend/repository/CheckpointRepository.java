package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Checkpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, Long> {
    // Fetch all checkpoints for a specific route
    List<Checkpoint> findByPatrolRouteId(Long patrolRouteId);
}