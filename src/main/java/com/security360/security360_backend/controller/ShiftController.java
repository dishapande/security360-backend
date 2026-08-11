package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.ShiftAssignmentDTO;
import com.security360.security360_backend.entity.Employee;
import com.security360.security360_backend.entity.Shift;
import com.security360.security360_backend.entity.ShiftAssignment;
import com.security360.security360_backend.repository.EmployeeRepository;
import com.security360.security360_backend.repository.ShiftAssignmentRepository;
import com.security360.security360_backend.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "http://localhost:8081")
public class ShiftController {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftAssignmentRepository assignmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. GET: Fetch all Shift Definitions
    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        return ResponseEntity.ok(shiftRepository.findAll());
    }

    // 2. POST: Create a new Shift Definition
    @PostMapping
    public ResponseEntity<String> createShift(@RequestBody Shift shift) {
        shiftRepository.save(shift);
        return ResponseEntity.ok("Shift created successfully!");
    }

    // 3. GET: Fetch Assignments for a specific date (For the Calendar View)
    @GetMapping("/assignments")
    public ResponseEntity<List<ShiftAssignmentDTO>> getAssignmentsByDate(@RequestParam String date) {
        LocalDate queryDate = LocalDate.parse(date);
        List<ShiftAssignment> assignments = assignmentRepository.findByAssignmentDate(queryDate);
        List<ShiftAssignmentDTO> response = new ArrayList<>();

        for (ShiftAssignment a : assignments) {
            ShiftAssignmentDTO dto = new ShiftAssignmentDTO();
            dto.setId(a.getId());
            dto.setEmployeeId(a.getEmployee().getId());
            dto.setEmployeeName(a.getEmployee().getFullName());
            dto.setShiftId(a.getShift().getId());
            dto.setShiftName(a.getShift().getShiftName());
            dto.setAssignmentDate(a.getAssignmentDate());
            dto.setStatus(a.getStatus());
            dto.setNotes(a.getNotes());
            response.add(dto);
        }
        return ResponseEntity.ok(response);
    }

    // 4. POST: Assign an Employee to a Shift
    @PostMapping("/assign")
    public ResponseEntity<String> assignShift(@RequestBody ShiftAssignmentDTO request) {
        try {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            Shift shift = shiftRepository.findById(request.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));

            ShiftAssignment assignment = new ShiftAssignment();
            assignment.setEmployee(employee);
            assignment.setShift(shift);
            assignment.setAssignmentDate(request.getAssignmentDate());
            assignment.setStatus("Scheduled");
            assignment.setNotes(request.getNotes());

            assignmentRepository.save(assignment);
            return ResponseEntity.ok("Shift assigned to " + employee.getFullName() + " successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error assigning shift: " + e.getMessage());
        }
    }

    // 5. PUT: Swap Shift (Requesting to swap)
    @PutMapping("/swap/{assignmentId}")
    public ResponseEntity<String> swapShift(@PathVariable Long assignmentId, @RequestParam Long targetEmployeeId) {
        try {
            ShiftAssignment existing = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));
            existing.setStatus("Pending Approval");
            // In a real app, you'd send a notification to the targetEmployeeId here
            assignmentRepository.save(existing);
            return ResponseEntity.ok("Swap request sent for approval.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error swapping shift: " + e.getMessage());
        }
    }

    // 6. PUT: Approve/Reject Shift Swap
    @PutMapping("/approve/{assignmentId}")
    public ResponseEntity<String> approveSwap(@PathVariable Long assignmentId, @RequestParam boolean approve) {
        try {
            ShiftAssignment existing = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));
            existing.setStatus(approve ? "Approved" : "Scheduled"); // Revert to Scheduled if rejected
            assignmentRepository.save(existing);
            return ResponseEntity.ok(approve ? "Shift swap approved." : "Shift swap rejected.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving shift: " + e.getMessage());
        }
    }
}