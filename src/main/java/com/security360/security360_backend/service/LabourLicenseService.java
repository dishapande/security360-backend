package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.LabourLicense;
import com.security360.security360_backend.repository.LabourLicenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LabourLicenseService {

    private final LabourLicenseRepository repository;

    public LabourLicenseService(LabourLicenseRepository repository) {
        this.repository = repository;
    }

    // GET ALL
    public List<LabourLicense> getAllLicenses() {
        return repository.findAll();
    }

    // GET BY ID
    public LabourLicense getLicenseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Labour License not found with id: " + id)
                );
    }

    // CREATE
    public LabourLicense createLicense(LabourLicense license) {

        if (license.getLicenseNumber() == null ||
                license.getLicenseNumber().trim().isEmpty()) {

            throw new RuntimeException("License Number is required");
        }

        if (license.getTitle() == null ||
                license.getTitle().trim().isEmpty()) {

            throw new RuntimeException("License Title is required");
        }

        if (license.getState() == null ||
                license.getState().trim().isEmpty()) {

            throw new RuntimeException("State is required");
        }

        if (license.getExpiryDate() == null) {
            throw new RuntimeException("Expiry Date is required");
        }

        String licenseNumber =
                license.getLicenseNumber().trim();

        if (repository.existsByLicenseNumber(licenseNumber)) {
            throw new RuntimeException(
                    "License Number already exists: " + licenseNumber
            );
        }

        license.setLicenseNumber(licenseNumber);

        if (license.getStatus() == null ||
                license.getStatus().trim().isEmpty()) {

            license.setStatus("Active");
        }

        if (license.getInspectionStatus() == null ||
                license.getInspectionStatus().trim().isEmpty()) {

            license.setInspectionStatus("Pending");
        }

        return repository.save(license);
    }

    // UPDATE
    public LabourLicense updateLicense(
            Long id,
            LabourLicense request
    ) {

        LabourLicense existing =
                getLicenseById(id);

        if (request.getLicenseNumber() != null &&
                !request.getLicenseNumber().trim().isEmpty()) {

            String newNumber =
                    request.getLicenseNumber().trim();

            if (!newNumber.equals(existing.getLicenseNumber())
                    && repository.existsByLicenseNumber(newNumber)) {

                throw new RuntimeException(
                        "License Number already exists: " + newNumber
                );
            }

            existing.setLicenseNumber(newNumber);
        }

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle().trim());
        }

        if (request.getState() != null) {
            existing.setState(request.getState().trim());
        }

        existing.setAuthority(request.getAuthority());
        existing.setOwner(request.getOwner());
        existing.setIssueDate(request.getIssueDate());
        existing.setExpiryDate(request.getExpiryDate());
        existing.setRenewalDate(request.getRenewalDate());
        existing.setInspectionDate(request.getInspectionDate());
        existing.setInspectionStatus(request.getInspectionStatus());
        existing.setStatus(request.getStatus());
        existing.setNotes(request.getNotes());
        existing.setDocumentName(request.getDocumentName());

        return repository.save(existing);
    }

    // RENEW
    public LabourLicense renewLicense(
            Long id,
            LocalDate renewalDate,
            LocalDate newExpiryDate
    ) {

        LabourLicense license =
                getLicenseById(id);

        if (renewalDate == null) {
            throw new RuntimeException(
                    "Renewal Date is required"
            );
        }

        if (newExpiryDate == null) {
            throw new RuntimeException(
                    "New Expiry Date is required"
            );
        }

        if (!newExpiryDate.isAfter(renewalDate)) {
            throw new RuntimeException(
                    "New Expiry Date must be after Renewal Date"
            );
        }

        license.setRenewalDate(renewalDate);
        license.setExpiryDate(newExpiryDate);
        license.setStatus("Active");

        return repository.save(license);
    }

    // DELETE
    public void deleteLicense(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Labour License not found with id: " + id
            );
        }

        repository.deleteById(id);
    }
}