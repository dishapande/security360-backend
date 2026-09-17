package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.QrCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCheckpointRepository
        extends JpaRepository<QrCheckpoint, Long> {

}