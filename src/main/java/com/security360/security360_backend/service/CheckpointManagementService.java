package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Checkpoint;
import com.security360.security360_backend.entity.CheckpointScan;
import com.security360.security360_backend.repository.CheckpointRepository;
import com.security360.security360_backend.repository.CheckpointScanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CheckpointManagementService {

    private final CheckpointRepository checkpointRepository;
    private final CheckpointScanRepository scanRepository;

    public CheckpointManagementService(
            CheckpointRepository checkpointRepository,
            CheckpointScanRepository scanRepository
    ) {
        this.checkpointRepository = checkpointRepository;
        this.scanRepository = scanRepository;
    }

    public List<Checkpoint> getAllCheckpoints() {
        return checkpointRepository.findAll();
    }

    public Checkpoint createCheckpoint(
            String name,
            String type,
            String site,
            String location
    ) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Checkpoint name is required"
            );
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Checkpoint type is required"
            );
        }

        String checkpointType = type.trim().toUpperCase();

        if (!checkpointType.equals("QR")
                && !checkpointType.equals("NFC")) {

            throw new IllegalArgumentException(
                    "Checkpoint type must be QR or NFC"
            );
        }

        if (site == null || site.isBlank()) {
            throw new IllegalArgumentException(
                    "Site is required"
            );
        }

        Checkpoint checkpoint = new Checkpoint();

        String prefix = checkpointType.equals("QR")
                ? "QR"
                : "NFC";

        String checkpointCode;

        do {
            checkpointCode =
                    prefix
                            + "-"
                            + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();

        } while (
                checkpointRepository
                        .findByCheckpointCode(checkpointCode)
                        .isPresent()
        );

        checkpoint.setCheckpointCode(checkpointCode);
        checkpoint.setName(name.trim());
        checkpoint.setType(checkpointType);
        checkpoint.setSite(site.trim());

        if (location != null) {
            checkpoint.setLocation(location.trim());
        }

        checkpoint.setStatus("Active");
        checkpoint.setLastScanStatus("Pending");

        return checkpointRepository.save(checkpoint);
    }

    public Checkpoint assignSite(
            Long id,
            String site
    ) {

        if (site == null || site.isBlank()) {
            throw new IllegalArgumentException(
                    "Site is required"
            );
        }

        Checkpoint checkpoint =
                checkpointRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Checkpoint not found"
                                )
                        );

        checkpoint.setSite(site.trim());

        return checkpointRepository.save(checkpoint);
    }

    @Transactional
    public Checkpoint scanCheckpoint(Long id) {

        Checkpoint checkpoint =
                checkpointRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Checkpoint not found"
                                )
                        );

        LocalDateTime now = LocalDateTime.now();

        checkpoint.setLastScan(now);
        checkpoint.setLastScanStatus("Scanned");

        checkpointRepository.save(checkpoint);

        CheckpointScan scan = new CheckpointScan();

        scan.setCheckpoint(checkpoint);
        scan.setScanTime(now);
        scan.setStatus("Completed");

        scanRepository.save(scan);

        return checkpoint;
    }

    public List<CheckpointScan> getScanHistory() {
        return scanRepository
                .findAllByOrderByScanTimeDesc();
    }

    public List<CheckpointScan> getCheckpointScanHistory(
            Long checkpointId
    ) {
        return scanRepository
                .findByCheckpointIdOrderByScanTimeDesc(
                        checkpointId
                );
    }

    public List<Checkpoint> getMissedCheckpoints() {
        return checkpointRepository
                .findByLastScanStatus("Missed");
    }

    public long getTotalCount() {
        return checkpointRepository.count();
    }

    public long getQrCount() {
        return checkpointRepository
                .findByType("QR")
                .size();
    }

    public long getNfcCount() {
        return checkpointRepository
                .findByType("NFC")
                .size();
    }

    public long getScanCount() {
        return scanRepository.count();
    }

    public long getMissedCount() {
        return checkpointRepository
                .findByLastScanStatus("Missed")
                .size();
    }
}