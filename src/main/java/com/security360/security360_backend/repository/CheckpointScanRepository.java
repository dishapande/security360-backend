package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.CheckpointScan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckpointScanRepository
        extends JpaRepository<CheckpointScan, Long> {

    List<CheckpointScan> findByCheckpointIdOrderByScanTimeDesc(Long checkpointId);

    List<CheckpointScan> findAllByOrderByScanTimeDesc();
}