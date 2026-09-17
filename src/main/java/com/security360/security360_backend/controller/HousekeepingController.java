package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.HousekeepingTask;
import com.security360.security360_backend.entity.HousekeepingIssue;
import com.security360.security360_backend.entity.HousekeepingStaff;
import com.security360.security360_backend.repository.HousekeepingTaskRepository;
import com.security360.security360_backend.repository.HousekeepingIssueRepository;
import com.security360.security360_backend.repository.HousekeepingStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/housekeeping")
@CrossOrigin(origins = "http://localhost:8081")
public class HousekeepingController {

    @Autowired private HousekeepingStaffRepository staffRepository;
    @Autowired private HousekeepingTaskRepository taskRepository;
    @Autowired private HousekeepingIssueRepository issueRepository;

    // STAFF MANAGEMENT
    @GetMapping("/staff")
    public ResponseEntity<List<HousekeepingStaff>> getAllStaff() { return ResponseEntity.ok(staffRepository.findAll()); }

    @PostMapping("/staff")
    public ResponseEntity<String> addStaff(@RequestBody HousekeepingStaff staff) {
        staffRepository.save(staff);
        return ResponseEntity.ok("Staff added successfully!");
    }

    // TASK MANAGEMENT
    @GetMapping("/tasks")
    public ResponseEntity<List<HousekeepingTask>> getAllTasks() { return ResponseEntity.ok(taskRepository.findAllByOrderByCreatedAtDesc()); }

    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@RequestBody HousekeepingTask task) {
        try {
            // 1. Use the transient staffId field
            Long staffId = task.getStaffId();
            
            if (staffId == null) {
                return ResponseEntity.badRequest().body("Staff ID is required!");
            }
            
            // 2. Find the staff from the database
            HousekeepingStaff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));
            
            // 3. Set the actual staff entity
            task.setStaff(staff);
            
            // 4. Save
            taskRepository.save(task);
            
            return ResponseEntity.ok("Task created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating task: " + e.getMessage());
        }
    }
    
    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        HousekeepingTask task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        taskRepository.save(task);
        return ResponseEntity.ok("Task status updated to: " + status);
    }

    // INSPECTION & SUPERVISOR REVIEW (Status update with notes)
    @PutMapping("/tasks/{id}/review")
    public ResponseEntity<String> reviewTask(@PathVariable Long id, @RequestParam String reviewNotes, @RequestParam boolean isApproved) {
        HousekeepingTask task = taskRepository.findById(id).orElseThrow();
        task.setStatus(isApproved ? "Completed" : "In Progress");
        taskRepository.save(task);
        return ResponseEntity.ok("Review recorded. Task " + (isApproved ? "Approved" : "Rejected") + ".");
    }

    // ISSUES & COMPLAINTS
    @GetMapping("/issues")
    public ResponseEntity<List<HousekeepingIssue>> getAllIssues() { return ResponseEntity.ok(issueRepository.findAll()); }

    @PostMapping("/issues")
    public ResponseEntity<String> reportIssue(@RequestBody HousekeepingIssue issue) {
        issueRepository.save(issue);
        return ResponseEntity.ok("Issue reported successfully!");
    }

    @PutMapping("/issues/{id}/resolve")
    public ResponseEntity<String> resolveIssue(@PathVariable Long id) {
        HousekeepingIssue issue = issueRepository.findById(id).orElseThrow();
        issue.setStatus("Resolved");
        issue.setResolvedAt(LocalDateTime.now());
        issueRepository.save(issue);
        return ResponseEntity.ok("Issue resolved successfully!");
    }

    // REPORTS
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getHousekeepingReport() {
        List<HousekeepingTask> tasks = taskRepository.findAll();
        long totalTasks = tasks.size();
        long pending = tasks.stream().filter(t -> "Pending".equalsIgnoreCase(t.getStatus())).count();
        long inProgress = tasks.stream().filter(t -> "In Progress".equalsIgnoreCase(t.getStatus())).count();
        long completed = tasks.stream().filter(t -> "Completed".equalsIgnoreCase(t.getStatus())).count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalTasks", totalTasks);
        response.put("pending", pending);
        response.put("inProgress", inProgress);
        response.put("completed", completed);
        response.put("tasks", tasks);
        return ResponseEntity.ok(response);
    }
}