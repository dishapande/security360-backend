package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Invoice;
import com.security360.security360_backend.repository.InvoiceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:8081")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // Get all invoices
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return ResponseEntity.ok(invoices);
    }

    // Create invoice
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
            @RequestBody Invoice invoice) {

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return ResponseEntity.ok(savedInvoice);
    }

    // Update payment status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Invoice> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Invoice invoice = invoiceRepository.findById(id).orElse(null);

        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        invoice.setStatus(status);

        Invoice updatedInvoice = invoiceRepository.save(invoice);

        return ResponseEntity.ok(updatedInvoice);
    }

    // Delete invoice
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(
            @PathVariable Long id) {

        if (!invoiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        invoiceRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}