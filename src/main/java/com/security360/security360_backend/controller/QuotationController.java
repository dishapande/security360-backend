package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Quotation;
import com.security360.security360_backend.repository.QuotationRepository;
import com.security360.security360_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "http://localhost:8081")
public class QuotationController {

    @Autowired private QuotationRepository quotationRepository;
    @Autowired private ClientRepository clientRepository;

    // 1. GET: Fetch all quotations
    @GetMapping
    public ResponseEntity<List<Quotation>> getAllQuotations() {
        return ResponseEntity.ok(quotationRepository.findAll());
    }

    // 2. POST: Create a new quotation
    @PostMapping
    public ResponseEntity<String> createQuotation(@RequestBody Quotation quotation) {
        try {
            // Validate client
            clientRepository.findById(quotation.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            // Auto-generate quotation number
            quotation.setQuotationNumber("QUO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

            // 🟢 FIX: Calculate total amount with tax (BigDecimal logic)
            BigDecimal amount = quotation.getAmount();
            BigDecimal taxPercent = quotation.getTaxPercent() != null ? quotation.getTaxPercent() : BigDecimal.valueOf(18);
            
            // Calculate: Total = Amount + (Amount * Tax / 100)
            BigDecimal taxAmount = amount.multiply(taxPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal totalAmount = amount.add(taxAmount);
            
            quotation.setTotalAmount(totalAmount);

            quotation.setStatus("Pending");
            quotationRepository.save(quotation);
            return ResponseEntity.ok("Quotation created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating quotation: " + e.getMessage());
        }
    }

    // 3. PUT: Approve a quotation
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveQuotation(@PathVariable Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id).orElseThrow();
            quotation.setStatus("Approved");
            quotation.setApprovedAt(LocalDateTime.now());
            quotationRepository.save(quotation);
            return ResponseEntity.ok("Quotation approved!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving quotation: " + e.getMessage());
        }
    }

    // 4. PUT: Reject a quotation
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectQuotation(@PathVariable Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id).orElseThrow();
            quotation.setStatus("Rejected");
            quotationRepository.save(quotation);
            return ResponseEntity.ok("Quotation rejected!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rejecting quotation: " + e.getMessage());
        }
    }

    // 5. PUT: Convert Quotation to Invoice (Links to Billing Module)
    @PutMapping("/{id}/convert-to-invoice")
    public ResponseEntity<String> convertToInvoice(@PathVariable Long id, @RequestParam Long invoiceId) {
        try {
            Quotation quotation = quotationRepository.findById(id).orElseThrow();
            quotation.setStatus("Converted");
            quotation.setConvertedAt(LocalDateTime.now());
            quotation.setInvoiceId(invoiceId); // Link to the invoice
            quotationRepository.save(quotation);
            return ResponseEntity.ok("Quotation converted to Invoice #" + invoiceId + " successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error converting quotation: " + e.getMessage());
        }
    }

    // 6. DELETE: Delete a quotation
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuotation(@PathVariable Long id) {
        quotationRepository.deleteById(id);
        return ResponseEntity.ok("Quotation deleted successfully!");
    }

    // 7. GET: Quotation Report
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getQuotationReport() {
        List<Quotation> quotations = quotationRepository.findAll();
        long totalQuotations = quotations.size();
        long pending = quotations.stream().filter(q -> "Pending".equalsIgnoreCase(q.getStatus())).count();
        long approved = quotations.stream().filter(q -> "Approved".equalsIgnoreCase(q.getStatus())).count();
        long converted = quotations.stream().filter(q -> "Converted".equalsIgnoreCase(q.getStatus())).count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalQuotations", totalQuotations);
        response.put("pending", pending);
        response.put("approved", approved);
        response.put("converted", converted);
        response.put("quotations", quotations);
        return ResponseEntity.ok(response);
    }
}