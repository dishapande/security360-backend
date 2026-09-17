package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Visitor;
import com.security360.security360_backend.service.VisitorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
@CrossOrigin(origins = {
        
        "http://localhost:8081"
})
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    // =========================================================
    // REGISTER VISITOR
    // =========================================================

    @PostMapping
    public ResponseEntity<Visitor> createVisitor(
            @RequestBody Visitor visitor
    ) {
        return ResponseEntity.ok(
                visitorService.createVisitor(visitor)
        );
    }

    // =========================================================
    // GET ALL VISITORS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Visitor>> getAllVisitors() {

        return ResponseEntity.ok(
                visitorService.getAllVisitors()
        );
    }

    // =========================================================
    // GET SINGLE VISITOR
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Visitor> getVisitor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                visitorService.getVisitorById(id)
        );
    }

    // =========================================================
    // APPROVE
    // =========================================================

    @PutMapping("/{id}/approve")
    public ResponseEntity<Visitor> approveVisitor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                visitorService.approveVisitor(id)
        );
    }

    // =========================================================
    // REJECT
    // =========================================================

    @PutMapping("/{id}/reject")
    public ResponseEntity<Visitor> rejectVisitor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                visitorService.rejectVisitor(id)
        );
    }

    // =========================================================
    // CHECK IN
    // =========================================================

    @PutMapping("/{id}/check-in")
    public ResponseEntity<Visitor> checkInVisitor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                visitorService.checkInVisitor(id)
        );
    }

    // =========================================================
    // CHECK OUT
    // =========================================================

    @PutMapping("/{id}/check-out")
    public ResponseEntity<Visitor> checkOutVisitor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                visitorService.checkOutVisitor(id)
        );
    }

    // =========================================================
    // BLACKLIST
    // =========================================================

    @PutMapping("/{id}/blacklist")
    public ResponseEntity<Visitor> blacklistVisitor(
            @PathVariable Long id,
            @RequestParam String reason
    ) {

        return ResponseEntity.ok(
                visitorService.blacklistVisitor(
                        id,
                        reason
                )
        );
    }

    // =========================================================
    // SEARCH
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<List<Visitor>> searchVisitors(
            @RequestParam String name
    ) {

        return ResponseEntity.ok(
                visitorService.searchVisitors(name)
        );
    }

    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Visitor>> getByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                visitorService.getVisitorsByStatus(status)
        );
    }

    // =========================================================
    // TODAY'S VISITORS
    // =========================================================

    @GetMapping("/today")
    public ResponseEntity<List<Visitor>> getTodayVisitors() {

        return ResponseEntity.ok(
                visitorService.getTodayVisitors()
        );
    }

    // =========================================================
    // REPORT
    // =========================================================

    @GetMapping("/reports")
    public ResponseEntity<List<Visitor>> getReports() {

        return ResponseEntity.ok(
                visitorService.getAllVisitors()
        );
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> dashboard() {

        Map<String, Long> response =
                new HashMap<>();

        response.put(
                "total",
                visitorService.getTotalVisitors()
        );

        response.put(
                "checkedIn",
                visitorService.getCheckedInVisitors()
        );

        response.put(
                "pending",
                visitorService.getPendingVisitors()
        );

        response.put(
                "checkedOut",
                visitorService.getCheckedOutVisitors()
        );

        response.put(
                "blacklisted",
                visitorService.getBlacklistedVisitors()
        );

        response.put(
                "approved",
                visitorService.getApprovedVisitors()
        );

        return ResponseEntity.ok(response);
    }
}