package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Expense;
import com.security360.security360_backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:8081")
public class ExpenseController {

    @Autowired private ExpenseRepository expenseRepository;

    // 1. GET: Fetch all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseRepository.findAllByOrderByCreatedAtDesc());
    }

    // 2. POST: Create a new expense
    @PostMapping
    public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
        expense.setStatus("Pending");
        expenseRepository.save(expense);
        return ResponseEntity.ok("Expense created successfully!");
    }

    // 3. PUT: Approve an expense
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveExpense(@PathVariable Long id, @RequestParam String approvedBy) {
        try {
            Expense expense = expenseRepository.findById(id).orElseThrow();
            expense.setStatus("Approved");
            expense.setApprovedBy(approvedBy);
            expense.setApprovedAt(LocalDateTime.now());
            expenseRepository.save(expense);
            return ResponseEntity.ok("Expense approved successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving expense: " + e.getMessage());
        }
    }

    // 4. PUT: Reject an expense
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectExpense(@PathVariable Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow();
        expense.setStatus("Rejected");
        expenseRepository.save(expense);
        return ResponseEntity.ok("Expense rejected successfully!");
    }

    // 5. DELETE: Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return ResponseEntity.ok("Expense deleted successfully!");
    }

    // 6. GET: Expense Report (By Category & Status)
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getExpenseReport() {
        List<Expense> expenses = expenseRepository.findAll();

        Map<String, Double> byCategory = new HashMap<>();
        for (Expense exp : expenses) {
            // ✅ FIX: Convert BigDecimal to double before adding
            byCategory.put(exp.getCategory(), byCategory.getOrDefault(exp.getCategory(), 0.0) + exp.getAmount().doubleValue());
        }

        // ✅ FIX: Use a manual loop instead of mapToDouble (BigDecimal cannot be directly converted)
        double totalExpenses = 0;
        for (Expense exp : expenses) {
            totalExpenses += exp.getAmount().doubleValue();
        }
        
        long approvedCount = expenses.stream().filter(e -> "Approved".equalsIgnoreCase(e.getStatus())).count();
        long pendingCount = expenses.stream().filter(e -> "Pending".equalsIgnoreCase(e.getStatus())).count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalExpenses", totalExpenses);
        response.put("approvedCount", approvedCount);
        response.put("pendingCount", pendingCount);
        response.put("byCategory", byCategory);
        response.put("expenses", expenses);
        return ResponseEntity.ok(response);
    }
}