package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Checkpoint;
import com.security360.security360_backend.entity.CheckpointScan;
import com.security360.security360_backend.service.CheckpointManagementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkpoints")
@CrossOrigin(origins = "http://localhost:8081")
public class CheckpointController {

    private final CheckpointManagementService checkpointService;

    public CheckpointController(
            CheckpointManagementService checkpointService
    ) {
        this.checkpointService = checkpointService;
    }

    @GetMapping
    public ResponseEntity<List<Checkpoint>> getAll() {

        return ResponseEntity.ok(
                checkpointService.getAllCheckpoints()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Map<String, String> request
    ) {

        try {

            Checkpoint checkpoint =
                    checkpointService.createCheckpoint(
                            request.get("name"),
                            request.get("type"),
                            request.get("site"),
                            request.get("location")
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(checkpoint);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "message",
                                    "Unable to create checkpoint"
                            )
                    );
        }
    }

    @PutMapping("/{id}/site")
    public ResponseEntity<?> assignSite(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {

        try {

            Checkpoint checkpoint =
                    checkpointService.assignSite(
                            id,
                            request.get("site")
                    );

            return ResponseEntity.ok(checkpoint);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/{id}/scan")
    public ResponseEntity<?> scan(
            @PathVariable Long id
    ) {

        try {

            Checkpoint checkpoint =
                    checkpointService.scanCheckpoint(id);

            return ResponseEntity.ok(checkpoint);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<CheckpointScan>> getHistory() {

        return ResponseEntity.ok(
                checkpointService.getScanHistory()
        );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<CheckpointScan>>
    getCheckpointHistory(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                checkpointService
                        .getCheckpointScanHistory(id)
        );
    }

    @GetMapping("/missed")
    public ResponseEntity<List<Checkpoint>> getMissed() {

        return ResponseEntity.ok(
                checkpointService
                        .getMissedCheckpoints()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put(
                "total",
                checkpointService.getTotalCount()
        );

        stats.put(
                "qr",
                checkpointService.getQrCount()
        );

        stats.put(
                "nfc",
                checkpointService.getNfcCount()
        );

        stats.put(
                "scans",
                checkpointService.getScanCount()
        );

        stats.put(
                "missed",
                checkpointService.getMissedCount()
        );

        return ResponseEntity.ok(stats);
    }
}