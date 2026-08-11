package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.IncidentTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentTimelineRepository extends JpaRepository<IncidentTimeline, Long> {
    
    // Fetch timeline for a specific incident, sorted newest first
    List<IncidentTimeline> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}