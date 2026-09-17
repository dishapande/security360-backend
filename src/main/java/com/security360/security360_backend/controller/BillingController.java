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
import java.util.HashMap;
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

    // =========================================================
    // 1. GET ALL BILLS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {

        List<Bill> bills =
                billRepo.findAllByOrderByCreatedAtDesc();

        return ResponseEntity.ok(
                mapToDTOList(bills)
        );
    }

    // =========================================================
    // 2. GENERATE NEW BILL
    // =========================================================

    @PostMapping("/generate")
    public ResponseEntity<String> generateBill(
            @RequestBody BillDTO dto) {

        if (dto == null) {
            return ResponseEntity.badRequest()
                    .body("Bill data is required.");
        }

        if (dto.getClientId() == null) {
            return ResponseEntity.badRequest()
                    .body("Client ID is required.");
        }

        if (dto.getAmount() == null) {
            return ResponseEntity.badRequest()
                    .body("Amount is required.");
        }

        Client client = clientRepo.findById(
                dto.getClientId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Client not found with ID: "
                                + dto.getClientId()
                )
        );

        // =====================================================
        // GENERATE INVOICE NUMBER
        // =====================================================

        String invoiceNum =
                "INV-"
                + YearMonth.now().format(
                        DateTimeFormatter.ofPattern("yyyyMM")
                )
                + "-"
                + (new Random().nextInt(9000) + 1000);

        // =====================================================
        // CREATE BILL
        // =====================================================

        Bill bill = new Bill();

        bill.setClient(client);

        bill.setInvoiceNumber(invoiceNum);

        bill.setBillingMonth(
                dto.getBillingMonth()
        );

        bill.setAmount(
                dto.getAmount()
        );

        // =====================================================
        // TAX
        // =====================================================

        BigDecimal taxPercent =
                dto.getTaxPercent() != null
                        ? dto.getTaxPercent()
                        : BigDecimal.ZERO;

        bill.setTaxPercent(taxPercent);

        // =====================================================
        // DISCOUNT
        // =====================================================

        BigDecimal discountPercent =
                dto.getDiscountPercent() != null
                        ? dto.getDiscountPercent()
                        : BigDecimal.ZERO;

        bill.setDiscountPercent(
                discountPercent
        );

        // =====================================================
        // CALCULATE TAX
        // =====================================================

        BigDecimal tax =
                bill.getAmount()
                        .multiply(
                                taxPercent.divide(
                                        BigDecimal.valueOf(100)
                                )
                        );

        // =====================================================
        // CALCULATE DISCOUNT
        // =====================================================

        BigDecimal discount =
                bill.getAmount()
                        .multiply(
                                discountPercent.divide(
                                        BigDecimal.valueOf(100)
                                )
                        );

        // =====================================================
        // TOTAL AMOUNT
        // =====================================================

        BigDecimal total =
                bill.getAmount()
                        .add(tax)
                        .subtract(discount);

        bill.setTotalAmount(total);

        // =====================================================
        // STATUS
        // =====================================================

        bill.setStatus("Pending");

        // =====================================================
        // DUE DATE
        // =====================================================

        bill.setDueDate(
                dto.getDueDate() != null
                        ? dto.getDueDate()
                        : LocalDate.now().plusDays(30)
        );

        // =====================================================
        // NOTES
        // =====================================================

        bill.setNotes(
                dto.getNotes() != null
                        ? dto.getNotes()
                        : ""
        );

        // =====================================================
        // SAVE BILL
        // =====================================================

        billRepo.save(bill);

        return ResponseEntity.ok(
                "Bill generated successfully! Invoice No: "
                        + invoiceNum
        );
    }

    // =========================================================
    // 3. MARK BILL AS PAID
    // =========================================================

    @PutMapping("/{id}/pay")
    public ResponseEntity<String> markAsPaid(
            @PathVariable Long id) {

        Bill bill = billRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill not found with ID: "
                                        + id
                        )
                );

        bill.setStatus("Paid");

        bill.setPaidAt(
                LocalDateTime.now()
        );

        billRepo.save(bill);

        return ResponseEntity.ok(
                "Bill marked as Paid."
        );
    }

    // =========================================================
    // 4. DASHBOARD STATS
    // =========================================================

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {

        long pending =
                billRepo.countByStatus("Pending");

        long paid =
                billRepo.countByStatus("Paid");

        long overdue =
                billRepo.countByStatus("Overdue");

        Map<String, Long> stats =
                new HashMap<>();

        stats.put("pending", pending);
        stats.put("paid", paid);
        stats.put("overdue", overdue);

        return ResponseEntity.ok(stats);
    }

    // =========================================================
    // 5. ENTITY -> DTO
    // =========================================================

    private List<BillDTO> mapToDTOList(
            List<Bill> bills) {

        List<BillDTO> result =
                new ArrayList<>();

        for (Bill b : bills) {

            BillDTO dto = new BillDTO();

            dto.setId(
                    b.getId()
            );

            // =================================================
            // CLIENT
            // =================================================

            if (b.getClient() != null) {

                dto.setClientId(
                        b.getClient().getId()
                );

                // Client मध्ये companyName आहे
                dto.setClientName(
                        b.getClient().getClientName()
                );
            }

            // =================================================
            // BILL INFORMATION
            // =================================================

            dto.setInvoiceNumber(
                    b.getInvoiceNumber()
            );

            dto.setBillingMonth(
                    b.getBillingMonth()
            );

            dto.setAmount(
                    b.getAmount()
            );

            dto.setTaxPercent(
                    b.getTaxPercent()
            );

            dto.setDiscountPercent(
                    b.getDiscountPercent()
            );

            dto.setTotalAmount(
                    b.getTotalAmount()
            );

            dto.setStatus(
                    b.getStatus()
            );

            dto.setDueDate(
                    b.getDueDate()
            );

            dto.setPaidAt(
                    b.getPaidAt()
            );

            dto.setNotes(
                    b.getNotes()
            );

            dto.setCreatedAt(
                    b.getCreatedAt()
            );

            result.add(dto);
        }

        return result;
    }
}