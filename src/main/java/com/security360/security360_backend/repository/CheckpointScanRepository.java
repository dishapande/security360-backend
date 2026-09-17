package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.CheckpointScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointScanRepository extends JpaRepository<CheckpointScan, Long> {

    List<CheckpointScan> findAllByOrderByScanTimeDesc();

    List<CheckpointScan> findByCheckpointIdOrderByScanTimeDesc(Long checkpointId);
}