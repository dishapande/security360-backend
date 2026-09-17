package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Visitor;
import com.security360.security360_backend.entity.Visitor.VisitorStatus;
import com.security360.security360_backend.repository.VisitorRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    // =========================================================
    // CREATE VISITOR
    // =========================================================

    public Visitor createVisitor(Visitor visitor) {

        if (visitor.getStatus() == null) {
            visitor.setStatus(VisitorStatus.Pending);
        }

        if (visitor.getVisitorId() == null ||
                visitor.getVisitorId().trim().isEmpty()) {

            visitor.setVisitorId(generateVisitorId());
        }

        if (visitor.getPassId() == null ||
                visitor.getPassId().trim().isEmpty()) {

            visitor.setPassId(generatePassId());
        }

        if (visitor.getVisitDate() == null) {
            visitor.setVisitDate(LocalDate.now());
        }

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // GET ALL VISITORS
    // =========================================================

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    // =========================================================
    // GET VISITOR BY ID
    // =========================================================

    public Visitor getVisitorById(Long id) {

        return visitorRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Visitor not found with id: " + id
                        )
                );
    }

    // =========================================================
    // UPDATE VISITOR
    // =========================================================

    public Visitor updateVisitor(Long id, Visitor updatedVisitor) {

        Visitor visitor = getVisitorById(id);

        visitor.setName(updatedVisitor.getName());
        visitor.setPhone(updatedVisitor.getPhone());
        visitor.setEmail(updatedVisitor.getEmail());
        visitor.setPurpose(updatedVisitor.getPurpose());
        visitor.setHost(updatedVisitor.getHost());
        visitor.setCompany(updatedVisitor.getCompany());
        visitor.setVisitDate(updatedVisitor.getVisitDate());
        visitor.setVehicleNumber(updatedVisitor.getVehicleNumber());
        visitor.setVehicleType(updatedVisitor.getVehicleType());
        visitor.setPhoto(updatedVisitor.getPhoto());
        visitor.setBlacklistReason(updatedVisitor.getBlacklistReason());

        if (updatedVisitor.getStatus() != null) {
            visitor.setStatus(updatedVisitor.getStatus());
        }

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // DELETE VISITOR
    // =========================================================

    public void deleteVisitor(Long id) {

        Visitor visitor = getVisitorById(id);

        visitorRepository.delete(visitor);
    }

    // =========================================================
    // APPROVE VISITOR
    // =========================================================

    public Visitor approveVisitor(Long id) {

        Visitor visitor = getVisitorById(id);

        visitor.setStatus(VisitorStatus.Approved);

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // REJECT VISITOR
    // =========================================================

    public Visitor rejectVisitor(Long id) {

        Visitor visitor = getVisitorById(id);

        visitor.setStatus(VisitorStatus.Rejected);

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // CHECK IN
    // =========================================================

    public Visitor checkInVisitor(Long id) {

        Visitor visitor = getVisitorById(id);

        if (visitor.getStatus() != VisitorStatus.Approved) {

            throw new RuntimeException(
                    "Visitor must be approved before check-in."
            );
        }

        visitor.setStatus(VisitorStatus.Checked_In);

        visitor.setCheckIn(getCurrentDateTime());

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // CHECK OUT
    // =========================================================

    public Visitor checkOutVisitor(Long id) {

        Visitor visitor = getVisitorById(id);

        if (visitor.getStatus() != VisitorStatus.Checked_In) {

            throw new RuntimeException(
                    "Visitor must be checked-in before check-out."
            );
        }

        visitor.setStatus(VisitorStatus.Checked_Out);

        visitor.setCheckOut(getCurrentDateTime());

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // BLACKLIST VISITOR
    // =========================================================

    public Visitor blacklistVisitor(Long id, String reason) {

        Visitor visitor = getVisitorById(id);

        visitor.setStatus(VisitorStatus.Blacklisted);

        visitor.setBlacklistReason(reason);

        return visitorRepository.save(visitor);
    }

    // =========================================================
    // SEARCH BY NAME
    // =========================================================

    public List<Visitor> searchVisitors(String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return visitorRepository.findAll();
        }

        return visitorRepository
                .findByNameContainingIgnoreCase(
                        keyword.trim()
                );
    }

    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    public List<Visitor> getVisitorsByStatus(String status) {

        VisitorStatus visitorStatus;

        try {

            visitorStatus =
                    VisitorStatus.valueOf(status);

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid visitor status: " + status
            );
        }

        return visitorRepository
                .findByStatus(visitorStatus);
    }

    // =========================================================
    // TODAY'S VISITORS
    // =========================================================

    public List<Visitor> getTodayVisitors() {

        LocalDate today = LocalDate.now();

        return visitorRepository
                .findByVisitDate(today);
    }

    // =========================================================
    // DASHBOARD - TOTAL
    // =========================================================

    public long getTotalVisitors() {

        return visitorRepository.count();
    }

    // =========================================================
    // DASHBOARD - PENDING
    // =========================================================

    public long getPendingVisitors() {

        return visitorRepository
                .countByStatus(VisitorStatus.Pending);
    }

    // =========================================================
    // DASHBOARD - APPROVED
    // =========================================================

    public long getApprovedVisitors() {

        return visitorRepository
                .countByStatus(VisitorStatus.Approved);
    }

    // =========================================================
    // DASHBOARD - CHECKED IN
    // =========================================================

    public long getCheckedInVisitors() {

        return visitorRepository
                .countByStatus(VisitorStatus.Checked_In);
    }

    // =========================================================
    // DASHBOARD - CHECKED OUT
    // =========================================================

    public long getCheckedOutVisitors() {

        return visitorRepository
                .countByStatus(VisitorStatus.Checked_Out);
    }

    // =========================================================
    // DASHBOARD - BLACKLISTED
    // =========================================================

    public long getBlacklistedVisitors() {

        return visitorRepository
                .countByStatus(VisitorStatus.Blacklisted);
    }

    // =========================================================
    // GENERATE VISITOR ID
    // =========================================================

    private String generateVisitorId() {

        long number = visitorRepository.count() + 1;

        return String.format(
                "VIS-%04d",
                number
        );
    }

    // =========================================================
    // GENERATE PASS ID
    // =========================================================

    private String generatePassId() {

        long number = visitorRepository.count() + 1;

        return String.format(
                "PASS-%04d",
                number
        );
    }

    // =========================================================
    // CURRENT DATE TIME
    // =========================================================

    private String getCurrentDateTime() {

        return LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                );
    }
}