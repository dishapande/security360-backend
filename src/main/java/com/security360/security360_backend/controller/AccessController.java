package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Door;
import com.security360.security360_backend.entity.AccessLog;
import com.security360.security360_backend.repository.DoorRepository;
import com.security360.security360_backend.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "http://localhost:8081")
public class AccessController {

    @Autowired private DoorRepository doorRepository;
    @Autowired private AccessLogRepository accessLogRepository;

    // 1. GET: Fetch all doors
    @GetMapping("/doors")
    public ResponseEntity<List<Door>> getAllDoors() {
        return ResponseEntity.ok(doorRepository.findAllByOrderByCreatedAtDesc());
    }

    // 2. POST: Create a new door
    @PostMapping("/doors")
    public ResponseEntity<String> createDoor(@RequestBody Door door) {
        doorRepository.save(door);
        return ResponseEntity.ok("Door created successfully!");
    }

    // 3. PUT: Lock/Unlock a door
    @PutMapping("/doors/{id}/status")
    public ResponseEntity<String> updateDoorStatus(@PathVariable Long id, @RequestParam String status) {
        Door door = doorRepository.findById(id).orElseThrow();
        door.setStatus(status);
        doorRepository.save(door);
        return ResponseEntity.ok("Door status updated to: " + status);
    }

    // 4. DELETE: Delete a door
    @DeleteMapping("/doors/{id}")
    public ResponseEntity<String> deleteDoor(@PathVariable Long id) {
        doorRepository.deleteById(id);
        return ResponseEntity.ok("Door deleted successfully!");
    }

    // 5. GET: Fetch all access logs
    @GetMapping("/logs")
    public ResponseEntity<List<AccessLog>> getAllLogs() {
        return ResponseEntity.ok(accessLogRepository.findAllByOrderByAccessTimeDesc());
    }

    // 6. POST: Record an access log (Finger, RFID, Card, etc.)
    @PostMapping("/logs")
    public ResponseEntity<String> createLog(@RequestBody AccessLog log) {
        try {
            // 🟢 FIX: Get the door from the database
            Long doorId = log.getDoor().getId(); // Extract door ID from request
            Door door = doorRepository.findById(doorId)
                    .orElseThrow(() -> new RuntimeException("Door not found with ID: " + doorId));

            // 🟢 FIX: Set the actual Door object
            log.setDoor(door);

            // Simulate access control logic
            if (log.isGranted()) {
                log.setReason("Access granted via " + log.getAccessMethod());
            } else {
                log.setReason("Access denied - No permission for " + log.getAccessMethod());
            }
            accessLogRepository.save(log);
            return ResponseEntity.ok("Access log recorded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating access log: " + e.getMessage());
        }
    }
}