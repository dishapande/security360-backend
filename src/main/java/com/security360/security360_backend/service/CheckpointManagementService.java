package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Checkpoint;
import com.security360.security360_backend.repository.CheckpointRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckpointManagementService {

    private final CheckpointRepository checkpointRepository;

    public CheckpointManagementService(
            CheckpointRepository checkpointRepository
    ) {
        this.checkpointRepository = checkpointRepository;
    }

    public List<Checkpoint> getAllCheckpoints() {
        return checkpointRepository.findAll();
    }

    /**
     * Create a checkpoint against the current entity model.
     * Fields supported by the entity: checkpointName, sequenceNumber,
     * latitude, longitude, qrCodeHash, patrolRoute.
     */
    public Checkpoint createCheckpoint(
            String checkpointName,
            Integer sequenceNumber,
            Double latitude,
            Double longitude,
            String qrCodeHash
    ) {
        if (checkpointName == null || checkpointName.isBlank()) {
            throw new IllegalArgumentException("Checkpoint name is required");
        }
        if (sequenceNumber == null || sequenceNumber < 1) {
            throw new IllegalArgumentException("Sequence number must be >= 1");
        }

        Checkpoint checkpoint = new Checkpoint();
        checkpoint.setCheckpointName(checkpointName.trim());
        checkpoint.setSequenceNumber(sequenceNumber);
        checkpoint.setLatitude(latitude);
        checkpoint.setLongitude(longitude);

        if (qrCodeHash != null && !qrCodeHash.isBlank()) {
            checkpoint.setQrCodeHash(qrCodeHash.trim());
        }

        return checkpointRepository.save(checkpoint);
    }

    public Checkpoint getCheckpoint(Long id) {
        return checkpointRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Checkpoint not found"));
    }

    public Checkpoint updateCheckpoint(
            Long id,
            String checkpointName,
            Integer sequenceNumber,
            Double latitude,
            Double longitude,
            String qrCodeHash
    ) {
        Checkpoint checkpoint = getCheckpoint(id);

        if (checkpointName != null && !checkpointName.isBlank()) {
            checkpoint.setCheckpointName(checkpointName.trim());
        }
        if (sequenceNumber != null && sequenceNumber >= 1) {
            checkpoint.setSequenceNumber(sequenceNumber);
        }
        if (latitude != null) {
            checkpoint.setLatitude(latitude);
        }
        if (longitude != null) {
            checkpoint.setLongitude(longitude);
        }
        if (qrCodeHash != null && !qrCodeHash.isBlank()) {
            checkpoint.setQrCodeHash(qrCodeHash.trim());
        }

        return checkpointRepository.save(checkpoint);
    }

    public void deleteCheckpoint(Long id) {
        if (!checkpointRepository.existsById(id)) {
            throw new RuntimeException("Checkpoint not found");
        }
        checkpointRepository.deleteById(id);
    }

    public long getTotalCount() {
        return checkpointRepository.count();
    }
}