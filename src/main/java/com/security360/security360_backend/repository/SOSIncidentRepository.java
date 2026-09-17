package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.SOSIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOSIncidentRepository extends JpaRepository<SOSIncident, Long> {
    // Fetch all incidents sorted by newest first
    java.util.List<SOSIncident> findAllByOrderByCreatedAtDesc();
}