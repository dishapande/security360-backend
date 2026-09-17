package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Holiday;
import com.security360.security360_backend.entity.LeaveRequest;
import com.security360.security360_backend.repository.HolidayRepository;
import com.security360.security360_backend.repository.LeaveRequestRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = {
        
        "http://localhost:8081"
})
public class LeaveController {

    private final LeaveRequestRepository leaveRequestRepository;
    private final HolidayRepository holidayRepository;

    public LeaveController(
            LeaveRequestRepository leaveRequestRepository,
            HolidayRepository holidayRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.holidayRepository = holidayRepository;
    }

    // =========================================================
    // GET ALL LEAVE REQUESTS
    // =========================================================

    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests() {

        List<LeaveRequest> requests =
                leaveRequestRepository.findAllByOrderByIdDesc();

        return ResponseEntity.ok(requests);
    }

    // =========================================================
    // GET PENDING REQUESTS
    // =========================================================

    @GetMapping("/requests/pending")
    public ResponseEntity<?> getPendingRequests() {

        return ResponseEntity.ok(
                leaveRequestRepository
                        .findByStatusOrderByIdDesc("Pending")
        );
    }

    // =========================================================
    // GET REQUEST BY ID
    // =========================================================

    @GetMapping("/requests/{id}")
    public ResponseEntity<?> getRequest(
            @PathVariable Long id
    ) {

        Optional<LeaveRequest> request =
                leaveRequestRepository.findById(id);

        if (request.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message("Leave request not found"));
        }

        return ResponseEntity.ok(request.get());
    }

    // =========================================================
    // CREATE LEAVE REQUEST
    // =========================================================

    @PostMapping("/requests")
    public ResponseEntity<?> createLeaveRequest(
            @RequestBody LeaveRequest request
    ) {

        if (request.getEmployee() == null ||
                request.getEmployee().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(message("Employee name is required"));
        }

        if (request.getLeaveType() == null ||
                request.getLeaveType().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(message("Leave type is required"));
        }

        if (request.getFromDate() == null ||
                request.getToDate() == null) {

            return ResponseEntity
                    .badRequest()
                    .body(message(
                            "From date and To date are required"
                    ));
        }

        if (request.getToDate()
                .isBefore(request.getFromDate())) {

            return ResponseEntity
                    .badRequest()
                    .body(message(
                            "To date cannot be before From date"
                    ));
        }

        long calculatedDays =
                ChronoUnit.DAYS.between(
                        request.getFromDate(),
                        request.getToDate()
                ) + 1;

        request.setDays((int) calculatedDays);

        request.setStatus("Pending");

        request.setRequestId(
                "LR-" + System.currentTimeMillis()
        );

        LeaveRequest saved =
                leaveRequestRepository.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // =========================================================
    // APPROVE LEAVE
    // =========================================================

    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<?> approveLeave(
            @PathVariable Long id
    ) {

        Optional<LeaveRequest> optional =
                leaveRequestRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message(
                            "Leave request not found"
                    ));
        }

        LeaveRequest request = optional.get();

        request.setStatus("Approved");

        LeaveRequest updated =
                leaveRequestRepository.save(request);

        return ResponseEntity.ok(updated);
    }

    // =========================================================
    // REJECT LEAVE
    // =========================================================

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<?> rejectLeave(
            @PathVariable Long id
    ) {

        Optional<LeaveRequest> optional =
                leaveRequestRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message(
                            "Leave request not found"
                    ));
        }

        LeaveRequest request = optional.get();

        request.setStatus("Rejected");

        LeaveRequest updated =
                leaveRequestRepository.save(request);

        return ResponseEntity.ok(updated);
    }

    // =========================================================
    // DELETE LEAVE REQUEST
    // =========================================================

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteLeave(
            @PathVariable Long id
    ) {

        if (!leaveRequestRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message(
                            "Leave request not found"
                    ));
        }

        leaveRequestRepository.deleteById(id);

        return ResponseEntity.ok(
                message(
                        "Leave request deleted successfully"
                )
        );
    }

    // =========================================================
    // GET ALL HOLIDAYS
    // =========================================================

    @GetMapping("/holidays")
    public ResponseEntity<?> getHolidays() {

        List<Holiday> holidays =
                holidayRepository
                        .findAllByOrderByDateAsc();

        return ResponseEntity.ok(holidays);
    }

    // =========================================================
    // ADD HOLIDAY
    // =========================================================

    @PostMapping("/holidays")
    public ResponseEntity<?> addHoliday(
            @RequestBody Holiday holiday
    ) {

        if (holiday.getDate() == null) {

            return ResponseEntity
                    .badRequest()
                    .body(message(
                            "Holiday date is required"
                    ));
        }

        if (holiday.getName() == null ||
                holiday.getName().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(message(
                            "Holiday name is required"
                    ));
        }

        // Prevent duplicate holiday date
        if (holidayRepository
                .existsByDate(holiday.getDate())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(message(
                            "Holiday already exists for this date"
                    ));
        }

        if (holiday.getHolidayType() == null ||
                holiday.getHolidayType().trim().isEmpty()) {

            holiday.setHolidayType("Public Holiday");
        }

        Holiday saved =
                holidayRepository.save(holiday);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // =========================================================
    // DELETE HOLIDAY
    // =========================================================

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<?> deleteHoliday(
            @PathVariable Long id
    ) {

        if (!holidayRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message(
                            "Holiday not found"
                    ));
        }

        holidayRepository.deleteById(id);

        return ResponseEntity.ok(
                message(
                        "Holiday deleted successfully"
                )
        );
    }

    // =========================================================
    // HOLIDAY REPORT BY DATE
    // =========================================================

    @GetMapping("/holidays/report")
    public ResponseEntity<?> getHolidayReport(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {

        if (from == null || to == null) {

            return ResponseEntity.ok(
                    holidayRepository
                            .findAllByOrderByDateAsc()
            );
        }

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        List<Holiday> holidays =
                holidayRepository
                        .findByDateBetweenOrderByDateAsc(
                                fromDate,
                                toDate
                        );

        return ResponseEntity.ok(holidays);
    }

    // =========================================================
    // DASHBOARD SUMMARY
    // =========================================================

    @GetMapping("/reports/summary")
    public ResponseEntity<?> getLeaveSummary() {

        List<LeaveRequest> requests =
                leaveRequestRepository.findAll();

        long total =
                requests.size();

        long pending =
                requests.stream()
                        .filter(r ->
                                "Pending".equals(
                                        r.getStatus()
                                ))
                        .count();

        long approved =
                requests.stream()
                        .filter(r ->
                                "Approved".equals(
                                        r.getStatus()
                                ))
                        .count();

        long rejected =
                requests.stream()
                        .filter(r ->
                                "Rejected".equals(
                                        r.getStatus()
                                ))
                        .count();

        long totalDays =
                requests.stream()
                        .filter(r ->
                                r.getDays() != null
                        )
                        .mapToLong(
                                r -> r.getDays()
                        )
                        .sum();

        Map<String, Object> summary =
                new HashMap<>();

        summary.put("totalRequests", total);
        summary.put("pending", pending);
        summary.put("approved", approved);
        summary.put("rejected", rejected);
        summary.put("totalLeaveDays", totalDays);

        return ResponseEntity.ok(summary);
    }

    // =========================================================
    // LEAVE REPORT BY DATE
    // =========================================================

    @GetMapping("/reports")
    public ResponseEntity<?> getReport(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {

        List<LeaveRequest> requests =
                leaveRequestRepository
                        .findAllByOrderByIdDesc();

        if (from == null || to == null) {
            return ResponseEntity.ok(requests);
        }

        LocalDate fromDate =
                LocalDate.parse(from);

        LocalDate toDate =
                LocalDate.parse(to);

        List<LeaveRequest> filtered =
                requests.stream()
                        .filter(request ->
                                request.getFromDate() != null &&
                                request.getToDate() != null &&
                                !request.getFromDate()
                                        .isAfter(toDate) &&
                                !request.getToDate()
                                        .isBefore(fromDate)
                        )
                        .toList();

        return ResponseEntity.ok(filtered);
    }

    // =========================================================
    // MESSAGE HELPER
    // =========================================================

    private Map<String, String> message(
            String text
    ) {

        Map<String, String> response =
                new HashMap<>();

        response.put("message", text);

        return response;
    }
}







