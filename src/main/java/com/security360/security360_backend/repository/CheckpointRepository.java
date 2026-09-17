package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Checkpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, Long> {

    Optional<Checkpoint> findByCheckpointCode(String checkpointCode);

    List<Checkpoint> findByType(String type);

    List<Checkpoint> findByLastScanStatus(String lastScanStatus);
}