package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.BillDTO;
import com.security360.security360_backend.entity.Bill;
import com.security360.security360_backend.entity.Client;
import com.security360.security360_backend.repository.BillRepository;
import com.security360.security360_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "http://localhost:8081")
public class BillingController {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private ClientRepository clientRepo;

    // 1. GET: Fetch all bills
    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<Bill> bills = billRepo.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(mapToDTOList(bills));
    }

    // 2. POST: Generate a new bill
    @PostMapping("/generate")
    public ResponseEntity<String> generateBill(@RequestBody BillDTO dto) {
        Client client = clientRepo.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + dto.getClientId()));

        // Auto-generate a unique Invoice Number
        String invoiceNum = "INV-" + YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + "-" + (new Random().nextInt(9999) + 1000);

        Bill bill = new Bill();
        bill.setClient(client);
        bill.setInvoiceNumber(invoiceNum);
        bill.setBillingMonth(dto.getBillingMonth());
        bill.setAmount(dto.getAmount());
        
        // Set Tax & Discount (default to 0 if null)
        bill.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : BigDecimal.ZERO);
        bill.setDiscountPercent(dto.getDiscountPercent() != null ? dto.getDiscountPercent() : BigDecimal.ZERO);
        
        // Calculate Total: (Amount + Tax) - Discount
        BigDecimal tax = bill.getAmount().multiply(bill.getTaxPercent().divide(BigDecimal.valueOf(100)));
        BigDecimal discount = bill.getAmount().multiply(bill.getDiscountPercent().divide(BigDecimal.valueOf(100)));
        BigDecimal total = bill.getAmount().add(tax).subtract(discount);
        bill.setTotalAmount(total);

        bill.setStatus("Pending");
        bill.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(30));
        bill.setNotes(dto.getNotes() != null ? dto.getNotes() : "");

        billRepo.save(bill);
        return ResponseEntity.ok("Bill generated successfully! Invoice No: " + invoiceNum);
    }

    // 3. PUT: Mark Bill as Paid
    @PutMapping("/{id}/pay")
    public ResponseEntity<String> markAsPaid(@PathVariable Long id) {
        Bill bill = billRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + id));
        bill.setStatus("Paid");
        bill.setPaidAt(LocalDateTime.now());
        billRepo.save(bill);
        return ResponseEntity.ok("Bill marked as Paid.");
    }
    // 4. GET: Dashboard Stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        long pending = billRepo.countByStatus("Pending");
        long paid = billRepo.countByStatus("Paid");
        long overdue = billRepo.countByStatus("Overdue");

        // 🟢 Using Map is safer and completely error-free
        Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("pending", pending);
        stats.put("paid", paid);
        stats.put("overdue", overdue);

        return ResponseEntity.ok(stats);
    }

    // --- Helper Method to convert Entity to DTO ---
    private List<BillDTO> mapToDTOList(List<Bill> bills) {
        List<BillDTO> result = new ArrayList<>();
        for (Bill b : bills) {
            BillDTO dto = new BillDTO();
            dto.setId(b.getId());
            dto.setClientId(b.getClient().getId());
            dto.setClientName(b.getClient().getName());
            dto.setInvoiceNumber(b.getInvoiceNumber());
            dto.setBillingMonth(b.getBillingMonth());
            dto.setAmount(b.getAmount());
            dto.setTaxPercent(b.getTaxPercent());
            dto.setDiscountPercent(b.getDiscountPercent());
            dto.setTotalAmount(b.getTotalAmount());
            dto.setStatus(b.getStatus());
            dto.setDueDate(b.getDueDate());
            dto.setPaidAt(b.getPaidAt());
            dto.setNotes(b.getNotes());
            dto.setCreatedAt(b.getCreatedAt());
            result.add(dto);
        }
        return result;
    }
}