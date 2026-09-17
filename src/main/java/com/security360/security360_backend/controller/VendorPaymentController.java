package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Vendor;
import com.security360.security360_backend.entity.VendorPayment;
import com.security360.security360_backend.repository.VendorPaymentRepository;
import com.security360.security360_backend.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:8081")
public class VendorPaymentController {

    @Autowired private VendorRepository vendorRepository;
    @Autowired private VendorPaymentRepository vendorPaymentRepository;

    // 1. GET: Fetch all vendors
    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorRepository.findAll());
    }

    // 2. POST: Add a new vendor
    @PostMapping
    public ResponseEntity<String> addVendor(@RequestBody Vendor vendor) {
        vendorRepository.save(vendor);
        return ResponseEntity.ok("Vendor added successfully!");
    }

    // 3. PUT: Update vendor
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVendor(@PathVariable Long id, @RequestBody Vendor vendorDetails) {
        return vendorRepository.findById(id)
                .map(existingVendor -> {
                    existingVendor.setVendorName(vendorDetails.getVendorName());
                    existingVendor.setContactPerson(vendorDetails.getContactPerson());
                    existingVendor.setEmail(vendorDetails.getEmail());
                    existingVendor.setPhone(vendorDetails.getPhone());
                    existingVendor.setAddress(vendorDetails.getAddress());
                    existingVendor.setGstNo(vendorDetails.getGstNo());
                    existingVendor.setCategory(vendorDetails.getCategory());
                    existingVendor.setStatus(vendorDetails.getStatus());
                    vendorRepository.save(existingVendor);
                    return ResponseEntity.ok("Vendor updated successfully!");
                })
                .orElse(ResponseEntity.badRequest().body("Vendor not found"));
    }

    // 4. DELETE: Delete a vendor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        vendorRepository.deleteById(id);
        return ResponseEntity.ok("Vendor deleted successfully!");
    }

    // 5. GET: Fetch all vendor payments
    @GetMapping("/payments")
    public ResponseEntity<List<VendorPayment>> getAllPayments() {
        return ResponseEntity.ok(vendorPaymentRepository.findAllByOrderByCreatedAtDesc());
    }

    // 6. POST: Record a new vendor payment (invoice)
    @PostMapping("/payments")
    public ResponseEntity<String> createPayment(@RequestBody VendorPayment payment) {
        // Auto-generate invoice number
        payment.setInvoiceNumber("VND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setStatus("Pending");
        vendorPaymentRepository.save(payment);
        return ResponseEntity.ok("Vendor invoice created successfully!");
    }

    // 7. PUT: Mark payment as PAID
    @PutMapping("/payments/{id}/pay")
    public ResponseEntity<String> markAsPaid(@PathVariable Long id, @RequestParam String paymentMethod) {
        VendorPayment payment = vendorPaymentRepository.findById(id).orElseThrow();
        payment.setStatus("Paid");
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(java.time.LocalDate.now());
        vendorPaymentRepository.save(payment);
        return ResponseEntity.ok("Payment marked as Paid successfully!");
    }

    // 8. GET: Vendor Payment Report
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getVendorReport() {
        List<Vendor> vendors = vendorRepository.findAll();
        List<VendorPayment> payments = vendorPaymentRepository.findAll();

        double totalPending = payments.stream().filter(p -> "Pending".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(p -> p.getAmount().doubleValue()).sum();
        double totalPaid = payments.stream().filter(p -> "Paid".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(p -> p.getAmount().doubleValue()).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalVendors", vendors.size());
        response.put("totalPending", totalPending);
        response.put("totalPaid", totalPaid);
        response.put("payments", payments);
        return ResponseEntity.ok(response);
    }
}