package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    // Fetch all incidents sorted by newest first
    List<Incident> findAllByOrderByCreatedAtDesc();

    // Count incidents by status (Used for Reports)
    long countByStatus(String status);
}