package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.SLAContract;
import com.security360.security360_backend.repository.SLAContractRepository;
import com.security360.security360_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/sla")
@CrossOrigin(origins = "http://localhost:8081")
public class SLAController {

    @Autowired private SLAContractRepository slaRepository;

    // 🟢 ADD THIS INJECTION
    @Autowired private ClientRepository clientRepository;

    // 1. GET: Fetch all SLAs
    @GetMapping
    public ResponseEntity<List<SLAContract>> getAllSLAs() {
        return ResponseEntity.ok(slaRepository.findAll());
    }

    // 2. POST: Create a new SLA
    @PostMapping
    public ResponseEntity<String> createSLA(@RequestBody SLAContract sla) {
        try {
            // 🟢 FIX: Verify client ID is present
            if (sla.getClient() == null || sla.getClient().getId() == null) {
                return ResponseEntity.badRequest().body("Client ID is required!");
            }
            
            // Validate that the client actually exists
            clientRepository.findById(sla.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + sla.getClient().getId()));
            
            slaRepository.save(sla);
            return ResponseEntity.ok("SLA created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating SLA: " + e.getMessage());
        }
    }

    // 3. PUT: Update SLA
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSLA(@PathVariable Long id, @RequestBody SLAContract slaDetails) {
        return slaRepository.findById(id)
                .map(existingSLA -> {
                    existingSLA.setSlaName(slaDetails.getSlaName());
                    existingSLA.setResponseTimeMinutes(slaDetails.getResponseTimeMinutes());
                    existingSLA.setPatrolsPerDay(slaDetails.getPatrolsPerDay());
                    existingSLA.setAttendancePercentage(slaDetails.getAttendancePercentage());
                    existingSLA.setPenaltyAmount(slaDetails.getPenaltyAmount());
                    slaRepository.save(existingSLA);
                    return ResponseEntity.ok("SLA updated successfully!");
                })
                .orElse(ResponseEntity.badRequest().body("SLA not found"));
    }

    // 4. GET: SLA Dashboard Report
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getSLADashboard() {
        List<SLAContract> slas = slaRepository.findAll();
        
        long completed = 0, missed = 0;
        double penalty = 0; // 🟢 Changed to double
        
        for (SLAContract sla : slas) {
            // Mock logic to determine status
            if (sla.getResponseTimeMinutes() < 5) completed++;
            else missed++;
            
            // ✅ FIX: Convert BigDecimal to double
            if (sla.getPenaltyAmount() != null) {
                penalty += sla.getPenaltyAmount().doubleValue();
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalSLAs", slas.size());
        response.put("completedSLAs", completed);
        response.put("missedSLAs", missed);
        response.put("totalPenalty", penalty);
        response.put("slas", slas);
        return ResponseEntity.ok(response);
    }
}