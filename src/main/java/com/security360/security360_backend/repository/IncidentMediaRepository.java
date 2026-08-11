package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.IncidentMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentMediaRepository extends JpaRepository<IncidentMedia, Long> {
    List<IncidentMedia> findByIncidentId(Long incidentId);
}