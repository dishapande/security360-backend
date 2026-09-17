package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Incident;
import com.security360.security360_backend.entity.IncidentMedia;
import com.security360.security360_backend.entity.IncidentTimeline;
import com.security360.security360_backend.repository.EmployeeRepository;
import com.security360.security360_backend.repository.IncidentMediaRepository;
import com.security360.security360_backend.repository.IncidentRepository;
import com.security360.security360_backend.repository.IncidentTimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:8081")
public class IncidentController {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentTimelineRepository incidentTimelineRepository;

    @Autowired
    private IncidentMediaRepository incidentMediaRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. GET: Fetch all incidents (Sorted by newest first)
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentRepository.findAllByOrderByCreatedAtDesc());
    }

    // 2. POST: Create a new incident
    @PostMapping
    public ResponseEntity<String> createIncident(@RequestBody Incident incident) {
        incident.setStatus("Open");
        incidentRepository.save(incident);
        return ResponseEntity.ok("Incident created successfully!");
    }

    // 3. GET: Fetch Report Data
    @GetMapping("/report-data")
    public ResponseEntity<Map<String, Object>> getIncidentReportData() {
        List<Incident> incidents = incidentRepository.findAll();

        long totalIncidents = incidents.size();
        long openIncidents = incidents.stream().filter(i -> "Open".equalsIgnoreCase(i.getStatus())).count();
        long inProgressIncidents = incidents.stream().filter(i -> "In Progress".equalsIgnoreCase(i.getStatus())).count();
        long escalatedIncidents = incidents.stream().filter(i -> "Escalated".equalsIgnoreCase(i.getStatus())).count();
        long resolvedIncidents = incidents.stream().filter(i -> "Resolved".equalsIgnoreCase(i.getStatus())).count();
        long criticalIncidents = incidents.stream().filter(i -> "Critical".equalsIgnoreCase(i.getPriority())).count();

        Map<String, Long> byCategory = new HashMap<>();
        for (Incident inc : incidents) {
            byCategory.put(inc.getCategory(), byCategory.getOrDefault(inc.getCategory(), 0L) + 1);
        }

        Map<String, Long> byPriority = new HashMap<>();
        for (Incident inc : incidents) {
            byPriority.put(inc.getPriority(), byPriority.getOrDefault(inc.getPriority(), 0L) + 1);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalIncidents", totalIncidents);
        response.put("openIncidents", openIncidents);
        response.put("inProgressIncidents", inProgressIncidents);
        response.put("escalatedIncidents", escalatedIncidents);
        response.put("resolvedIncidents", resolvedIncidents);
        response.put("criticalIncidents", criticalIncidents);
        response.put("byCategory", byCategory);
        response.put("byPriority", byPriority);
        response.put("incidents", incidents);

        return ResponseEntity.ok(response);
    }

    // 4. GET: Fetch Timeline for a specific incident
    @GetMapping("/{id}/timeline")
    public ResponseEntity<List<IncidentTimeline>> getTimeline(@PathVariable Long id) {
        return ResponseEntity.ok(incidentTimelineRepository.findByIncidentIdOrderByCreatedAtDesc(id));
    }

    // 5. POST: Upload media to an incident
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadMedia(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/incident-files/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

            Incident incident = incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found"));

            IncidentMedia media = new IncidentMedia();
            media.setIncident(incident);
            media.setFileUrl("/incident-files/" + fileName);
            media.setFileType(file.getContentType());

            incidentMediaRepository.save(media);

            return ResponseEntity.ok("File uploaded: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    // 6. PUT: Update Incident Status
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status, @RequestParam String note) {
        Incident incident = incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(status);
        incidentRepository.save(incident);

        // Add timeline entry
        IncidentTimeline timeline = new IncidentTimeline();
        timeline.setIncident(incident);
        timeline.setAction("Status Updated to " + status);
        timeline.setNote(note);
        timeline.setPerformedBy("System");
        timeline.setCreatedAt(LocalDateTime.now());
        incidentTimelineRepository.save(timeline);

        return ResponseEntity.ok("Status updated to: " + status);
    }

    // 7. PUT: Assign Officer
    @PutMapping("/{id}/assign")
    public ResponseEntity<String> assignOfficer(@PathVariable Long id, @RequestParam Long officerId) {
        Incident incident = incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found"));
        employeeRepository.findById(officerId).ifPresent(emp -> {
            incident.setAssignedOfficerId(emp.getId());
            incident.setAssignedOfficerName(emp.getFullName());
            incidentRepository.save(incident);
        });
        return ResponseEntity.ok("Officer assigned successfully.");
    }
}