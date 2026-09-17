package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Contract;
import com.security360.security360_backend.repository.ClientRepository;
import com.security360.security360_backend.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:8081")
public class ContractController {

    @Autowired private ContractRepository contractRepository;
    @Autowired private ClientRepository clientRepository;

    // 1. GET: Fetch all contracts
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractRepository.findAllByOrderByCreatedAtDesc());
    }

    // 2. GET: Fetch contracts expiring within 30 days (Alerts)
    @GetMapping("/expiring")
    public ResponseEntity<List<Contract>> getExpiringContracts() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        return ResponseEntity.ok(contractRepository.findByEndDateBetween(today, thirtyDaysLater));
    }

    // 3. POST: Create a new contract
    @PostMapping
    public ResponseEntity<String> createContract(@RequestBody Contract contract) {
        try {
            // Validate client exists
            clientRepository.findById(contract.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            // Auto-generate contract number if missing
            if (contract.getContractNumber() == null || contract.getContractNumber().isEmpty()) {
                contract.setContractNumber("CON-" + System.currentTimeMillis());
            }

            contract.setStatus("Active");
            contractRepository.save(contract);
            return ResponseEntity.ok("Contract created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating contract: " + e.getMessage());
        }
    }

    // 4. PUT: Renew a contract
    @PutMapping("/{id}/renew")
    public ResponseEntity<String> renewContract(@PathVariable Long id, @RequestParam String newEndDate) {
        Contract contract = contractRepository.findById(id).orElseThrow();
        contract.setEndDate(LocalDate.parse(newEndDate));
        contract.setStatus("Active");
        contractRepository.save(contract);
        return ResponseEntity.ok("Contract renewed successfully!");
    }

    // 5. PUT: Update contract status
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Contract contract = contractRepository.findById(id).orElseThrow();
        contract.setStatus(status);
        contractRepository.save(contract);
        return ResponseEntity.ok("Contract status updated to: " + status);
    }

    // 6. POST: Upload agreement file
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadAgreement(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/contract-files/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + fileName));

            Contract contract = contractRepository.findById(id).orElseThrow();
            contract.setAgreementFileUrl("/contract-files/" + fileName);
            contractRepository.save(contract);
            return ResponseEntity.ok("Agreement uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    // 7. DELETE: Delete a contract
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContract(@PathVariable Long id) {
        contractRepository.deleteById(id);
        return ResponseEntity.ok("Contract deleted successfully!");
    }

    // 8. GET: Contract Report
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getContractReport() {
        List<Contract> contracts = contractRepository.findAll();
        long totalContracts = contracts.size();
        long activeContracts = contracts.stream().filter(c -> "Active".equalsIgnoreCase(c.getStatus())).count();
        long expiredContracts = contracts.stream().filter(c -> "Expired".equalsIgnoreCase(c.getStatus())).count();
        long pendingRenewals = contracts.stream().filter(c -> "Pending Renewal".equalsIgnoreCase(c.getStatus())).count();
        double totalPricing = contracts.stream().mapToDouble(c -> c.getPricing() != null ? c.getPricing() : 0).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalContracts", totalContracts);
        response.put("activeContracts", activeContracts);
        response.put("expiredContracts", expiredContracts);
        response.put("pendingRenewals", pendingRenewals);
        response.put("totalPricing", totalPricing);
        response.put("contracts", contracts);
        return ResponseEntity.ok(response);
    }
}