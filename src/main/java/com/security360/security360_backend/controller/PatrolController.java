package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.PatrolScanDTO;
import com.security360.security360_backend.entity.*;
import com.security360.security360_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patrol")
@CrossOrigin(origins = "http://localhost:8081")
public class PatrolController {

    @Autowired private PatrolRouteRepository routeRepository;
    @Autowired private CheckpointRepository checkpointRepository;
    @Autowired private PatrolSessionRepository sessionRepository;
    @Autowired private PatrolLogRepository logRepository;
    @Autowired private EmployeeRepository employeeRepository;

    // 1. GET: Fetch all patrol routes
    @GetMapping("/routes")
    public ResponseEntity<List<PatrolRoute>> getAllRoutes() {
        return ResponseEntity.ok(routeRepository.findAll());
    }

    // 2. POST: Create a new route with checkpoints
    @PostMapping("/routes")
    public ResponseEntity<String> createRoute(@RequestBody PatrolRoute route) {
        routeRepository.save(route);
        return ResponseEntity.ok("Route created successfully!");
    }

    // 3. POST: Add a checkpoint to a route
    @PostMapping("/checkpoints")
    public ResponseEntity<String> addCheckpoint(@RequestBody Checkpoint checkpoint) {
        checkpointRepository.save(checkpoint);
        return ResponseEntity.ok("Checkpoint added!");
    }

    // 4. POST: Start a new patrol session
    @PostMapping("/start/{employeeId}/{routeId}")
    public ResponseEntity<PatrolSession> startPatrol(@PathVariable Long employeeId, @PathVariable Long routeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        PatrolRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // Close any previous in-progress sessions for this guard
        Optional<PatrolSession> openSession = sessionRepository.findByEmployeeIdAndStatus(employeeId, "In Progress");
        openSession.ifPresent(session -> {
            session.setStatus("Missed");
            session.setEndTime(LocalDateTime.now());
            sessionRepository.save(session);
        });

        PatrolSession newSession = new PatrolSession();
        newSession.setEmployee(employee);
        newSession.setPatrolRoute(route);
        newSession.setStartTime(LocalDateTime.now());
        newSession.setStatus("In Progress");
        
        return ResponseEntity.ok(sessionRepository.save(newSession));
    }

    // 5. POST: Scan a checkpoint (QR, NFC, or Manual)
    @PostMapping("/scan")
    public ResponseEntity<String> scanCheckpoint(@RequestBody PatrolScanDTO request) {
        PatrolSession session = sessionRepository.findById(request.getPatrolSessionId())
                .orElseThrow(() -> new RuntimeException("Patrol session not found"));

        Checkpoint checkpoint = checkpointRepository.findById(request.getCheckpointId())
                .orElseThrow(() -> new RuntimeException("Checkpoint not found"));

        // Calculate if the guard is LATE
        LocalDateTime now = LocalDateTime.now();
        long lateSeconds = 0;
        // Simple mock: If scan time is > 15 minutes since start, they are late.
        if (Duration.between(session.getStartTime(), now).toMinutes() > 15) {
            lateSeconds = Duration.between(session.getStartTime().plusMinutes(15), now).getSeconds();
        }

        PatrolLog log = new PatrolLog();
        log.setPatrolSession(session);
        log.setCheckpoint(checkpoint);
        log.setScanTime(now);
        log.setScanMethod(request.getScanMethod());
        log.setGuardNotes(request.getGuardNotes());
        log.setLateSeconds((int) lateSeconds);
        
        // Handle Photo (In a real app, save base64 to a file folder similar to Face Capture)
        // For now, we just accept it.
        log.setPhotoUrl("scanned_at_" + now.toString());

        logRepository.save(log);

        String statusMessage = lateSeconds > 0 ? "Checkpoint scanned, but you are " + lateSeconds + " seconds LATE!" : "Checkpoint scanned on time.";
        return ResponseEntity.ok(statusMessage);
    }

    // 6. GET: Get the full details of an active patrol (For Supervisor Review)
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<PatrolSession> getPatrolDetails(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionRepository.findById(sessionId).orElseThrow());
    }

    // 7. PUT: Supervisor Review / Complete Patrol
    @PutMapping("/complete/{sessionId}")
    public ResponseEntity<String> completePatrol(
            @PathVariable Long sessionId, 
            @RequestParam String supervisorNotes, 
            @RequestParam boolean isApproved) {
        
        PatrolSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setEndTime(LocalDateTime.now());
        session.setStatus(isApproved ? "Completed" : "Missed");
        session.setSupervisorNotes(supervisorNotes);
        session.setSupervisorReviewed(true);
        
        sessionRepository.save(session);
        return ResponseEntity.ok("Patrol " + (isApproved ? "Completed" : "Marked Missed") + " by Supervisor.");
    }
    
    // 8. GET: Fetch all patrol sessions (For the History Table)
    @GetMapping("/sessions")
    public ResponseEntity<List<PatrolSession>> getAllPatrolSessions() {
        // Optional: You can add sorting if you want
        return ResponseEntity.ok(sessionRepository.findAllByOrderByStartTimeDesc());
    }
}