package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingSessionRepository
        extends JpaRepository<TrainingSession, Long> {

    List<TrainingSession> findByDateBetween(String from, String to);
}