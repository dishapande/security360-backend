package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.SOSIncident;
import com.security360.security360_backend.repository.SOSIncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sos")
@CrossOrigin(origins = "http://localhost:8081")
public class SOSController {

    @Autowired private SOSIncidentRepository repo;

    // 1. GET: Fetch all incidents
    @GetMapping
    public ResponseEntity<List<SOSIncident>> getAll() {
        return ResponseEntity.ok(repo.findAllByOrderByCreatedAtDesc());
    }

    // 2. POST: Create a new SOS incident
    @PostMapping
    public ResponseEntity<String> create(@RequestBody SOSIncident incident) {
        incident.setStatus("Active");
        incident.setCreatedAt(LocalDateTime.now());
        repo.save(incident);
        return ResponseEntity.ok("SOS Alert Created Successfully");
    }

    // 3. PUT: Assign a response team
    @PutMapping("/{id}/assign")
    public ResponseEntity<String> assign(@PathVariable Long id, @RequestParam String team) {
        SOSIncident sos = repo.findById(id).orElseThrow(() -> new RuntimeException("SOS not found"));
        sos.setAssignedTeam(team);
        sos.setStatus("Assigned");
        sos.setResponseTime(LocalDateTime.now()); // Captures when the team was assigned
        repo.save(sos);
        return ResponseEntity.ok("Response Team Assigned: " + team);
    }

    // 4. PUT: Resolve the incident
    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> resolve(@PathVariable Long id) {
        SOSIncident sos = repo.findById(id).orElseThrow(() -> new RuntimeException("SOS not found"));
        sos.setStatus("Resolved");
        sos.setResolvedTime(LocalDateTime.now());
        repo.save(sos);
        return ResponseEntity.ok("Incident Resolved Successfully");
    }
}