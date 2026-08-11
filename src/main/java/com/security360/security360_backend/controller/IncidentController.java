package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.IncidentDTO;
import com.security360.security360_backend.dto.TimelineDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:8081")
public class IncidentController {

    @Autowired private IncidentRepository incidentRepo;
    @Autowired private IncidentTimelineRepository timelineRepo;
    @Autowired private IncidentMediaRepository mediaRepo;
    @Autowired private EmployeeRepository empRepo;

    // 1. GET: Fetch all incidents
    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        List<Incident> incidents = incidentRepo.findAllByOrderByCreatedAtDesc();
        List<IncidentDTO> response = new ArrayList<>();
        for (Incident inc : incidents) {
            response.add(mapToDTO(inc));
        }
        return ResponseEntity.ok(response);
    }

    // 2. POST: Create Incident
    @PostMapping
    public ResponseEntity<String> createIncident(@RequestBody IncidentDTO dto) {
        Incident inc = new Incident();
        inc.setTitle(dto.getTitle());
        inc.setDescription(dto.getDescription());
        inc.setCategory(dto.getCategory());
        inc.setPriority(dto.getPriority());
        inc.setStatus("Open");
        inc.setLocation(dto.getLocation());
        inc.setReportedBy(dto.getReportedBy() != null ? dto.getReportedBy() : "System User");
        
        // Assign officer if provided
        if (dto.getAssignedOfficerId() != null) {
            empRepo.findById(dto.getAssignedOfficerId()).ifPresent(emp -> {
                inc.setAssignedOfficerId(emp.getId());
                inc.setAssignedOfficerName(emp.getFullName());
            });
        }

        Incident saved = incidentRepo.save(inc);
        
        // Add Timeline entry: Created
        addTimeline(saved.getId(), "Created", "Incident reported: " + dto.getTitle(), "System");
        
        return ResponseEntity.ok("Incident created successfully with ID: " + saved.getId());
    }

    // 3. PUT: Update Status (Resolve, Close, Escalate)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status, @RequestParam String note) {
        Incident inc = incidentRepo.findById(id).orElseThrow();
        inc.setStatus(status);
        incidentRepo.save(inc);
        addTimeline(id, "Status Updated to " + status, note, "System");
        return ResponseEntity.ok("Status updated to: " + status);
    }

    // 4. PUT: Assign Officer
    @PutMapping("/{id}/assign")
    public ResponseEntity<String> assignOfficer(@PathVariable Long id, @RequestParam Long officerId) {
        Incident inc = incidentRepo.findById(id).orElseThrow();
        empRepo.findById(officerId).ifPresent(emp -> {
            inc.setAssignedOfficerId(emp.getId());
            inc.setAssignedOfficerName(emp.getFullName());
            incidentRepo.save(inc);
            addTimeline(id, "Assigned", "Assigned to " + emp.getFullName(), "System");
        });
        return ResponseEntity.ok("Officer assigned successfully.");
    }

    // 5. POST: Upload Media (Image/Video)
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadMedia(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/incident-files/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

            // Save to DB
            IncidentMedia media = new IncidentMedia();
            media.setIncident(incidentRepo.findById(id).orElseThrow());
            media.setFileUrl("/incident-files/" + fileName);
            media.setFileType(file.getContentType());
            mediaRepo.save(media);

            addTimeline(id, "Media Uploaded", "File uploaded: " + fileName, "System");
            return ResponseEntity.ok("File uploaded: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    // 6. GET: Fetch Timeline
    @GetMapping("/{id}/timeline")
    public ResponseEntity<List<TimelineDTO>> getTimeline(@PathVariable Long id) {
        List<IncidentTimeline> logs = timelineRepo.findByIncidentIdOrderByCreatedAtDesc(id);
        List<TimelineDTO> response = new ArrayList<>();
        for (IncidentTimeline log : logs) {
            TimelineDTO dto = new TimelineDTO();
            dto.setId(log.getId());
            dto.setAction(log.getAction());
            dto.setNote(log.getNote());
            dto.setPerformedBy(log.getPerformedBy());
            dto.setCreatedAt(log.getCreatedAt());
            response.add(dto);
        }
        return ResponseEntity.ok(response);
    }

    // 7. GET: Generate Report (Mock data for demo)
    @GetMapping("/report")
    public ResponseEntity<String> generateReport() {
        long open = incidentRepo.countByStatus("Open");
        long resolved = incidentRepo.countByStatus("Resolved");
        long escalated = incidentRepo.countByStatus("Escalated");
        return ResponseEntity.ok("{\"open\": " + open + ", \"resolved\": " + resolved + ", \"escalated\": " + escalated + "}");
    }

    // --- Helper ---
    private void addTimeline(Long incidentId, String action, String note, String by) {
        IncidentTimeline tl = new IncidentTimeline();
        tl.setIncident(incidentRepo.findById(incidentId).orElseThrow());
        tl.setAction(action);
        tl.setNote(note);
        tl.setPerformedBy(by);
        tl.setCreatedAt(LocalDateTime.now());
        timelineRepo.save(tl);
    }

    private IncidentDTO mapToDTO(Incident inc) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(inc.getId());
        dto.setTitle(inc.getTitle());
        dto.setDescription(inc.getDescription());
        dto.setCategory(inc.getCategory());
        dto.setPriority(inc.getPriority());
        dto.setStatus(inc.getStatus());
        dto.setLocation(inc.getLocation());
        dto.setReportedBy(inc.getReportedBy());
        dto.setAssignedOfficerId(inc.getAssignedOfficerId());
        dto.setAssignedOfficerName(inc.getAssignedOfficerName());
        dto.setCreatedAt(inc.getCreatedAt());
        
        List<String> mediaUrls = new ArrayList<>();
        for (IncidentMedia m : inc.getMediaFiles()) {
            mediaUrls.add(m.getFileUrl());
        }
        dto.setMediaUrls(mediaUrls);
        return dto;
    }
}