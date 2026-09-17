package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.PoliceVerification;
import com.security360.security360_backend.repository.PoliceVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PoliceVerificationService {

    private final PoliceVerificationRepository repository;

    public PoliceVerificationService(
            PoliceVerificationRepository repository
    ) {
        this.repository = repository;
    }

    public List<PoliceVerification> getAll() {
        return repository.findAll();
    }

    public PoliceVerification getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Police verification record not found with id: " + id
                        )
                );
    }

    public List<PoliceVerification> search(String search) {

        if (search == null || search.trim().isEmpty()) {
            return repository.findAll();
        }

        String keyword = search.trim();

        List<PoliceVerification> byName =
                repository.findByEmployeeNameContainingIgnoreCase(keyword);

        if (!byName.isEmpty()) {
            return byName;
        }

        return repository.findByEmployeeIdContainingIgnoreCase(keyword);
    }

    public List<PoliceVerification> getByStatus(String status) {

        if (status == null || status.trim().isEmpty()) {
            return repository.findAll();
        }

        return repository.findByVerificationStatusIgnoreCase(status);
    }

    public PoliceVerification create(
            PoliceVerification verification,
            MultipartFile file
    ) throws IOException {

        validate(verification);

        if (file != null && !file.isEmpty()) {
            validateFile(file);

            verification.setDocumentFileName(file.getOriginalFilename());
            verification.setDocumentContentType(file.getContentType());
            verification.setDocumentData(file.getBytes());

            if (
                    verification.getDocumentName() == null ||
                    verification.getDocumentName().trim().isEmpty()
            ) {
                verification.setDocumentName(file.getOriginalFilename());
            }
        }

        return repository.save(verification);
    }

    public PoliceVerification update(
            Long id,
            PoliceVerification updated,
            MultipartFile file
    ) throws IOException {

        PoliceVerification existing = getById(id);

        validate(updated);

        existing.setEmployeeName(updated.getEmployeeName());
        existing.setEmployeeId(updated.getEmployeeId());
        existing.setVerificationStatus(
                updated.getVerificationStatus()
        );
        existing.setVerificationDate(
                updated.getVerificationDate()
        );
        existing.setPoliceStation(
                updated.getPoliceStation()
        );
        existing.setDocumentName(
                updated.getDocumentName()
        );
        existing.setDocumentExpiry(
                updated.getDocumentExpiry()
        );
        existing.setRemarks(
                updated.getRemarks()
        );

        if (file != null && !file.isEmpty()) {

            validateFile(file);

            existing.setDocumentFileName(
                    file.getOriginalFilename()
            );

            existing.setDocumentContentType(
                    file.getContentType()
            );

            existing.setDocumentData(
                    file.getBytes()
            );

            if (
                    existing.getDocumentName() == null ||
                    existing.getDocumentName().trim().isEmpty()
            ) {
                existing.setDocumentName(
                        file.getOriginalFilename()
                );
            }
        }

        return repository.save(existing);
    }

    public void delete(Long id) {

        PoliceVerification existing = getById(id);

        repository.delete(existing);
    }

    public byte[] getDocument(Long id) {

        PoliceVerification verification = getById(id);

        if (
                verification.getDocumentData() == null ||
                verification.getDocumentData().length == 0
        ) {
            throw new RuntimeException(
                    "No document uploaded for this record"
            );
        }

        return verification.getDocumentData();
    }

    public String getDocumentFileName(Long id) {
        PoliceVerification verification = getById(id);

        if (verification.getDocumentFileName() == null) {
            return "police-verification-document";
        }

        return verification.getDocumentFileName();
    }

    public String getDocumentContentType(Long id) {
        PoliceVerification verification = getById(id);

        if (verification.getDocumentContentType() == null) {
            return "application/octet-stream";
        }

        return verification.getDocumentContentType();
    }

    private void validate(
            PoliceVerification verification
    ) {

        if (
                verification.getEmployeeName() == null ||
                verification.getEmployeeName().trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Employee name is required"
            );
        }

        if (
                verification.getEmployeeId() == null ||
                verification.getEmployeeId().trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Employee ID is required"
            );
        }

        if (
                verification.getVerificationStatus() == null ||
                verification.getVerificationStatus().trim().isEmpty()
        ) {
            verification.setVerificationStatus("Pending");
        }

        if (
                "Verified".equalsIgnoreCase(
                        verification.getVerificationStatus()
                )
                &&
                verification.getVerificationDate() == null
        ) {
            throw new IllegalArgumentException(
                    "Verification date is required for verified records"
            );
        }

        LocalDate verificationDate =
                verification.getVerificationDate();

        LocalDate expiryDate =
                verification.getDocumentExpiry();

        if (
                verificationDate != null &&
                expiryDate != null &&
                expiryDate.isBefore(verificationDate)
        ) {
            throw new IllegalArgumentException(
                    "Document expiry date cannot be earlier than verification date"
            );
        }
    }

    private void validateFile(
            MultipartFile file
    ) {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "Invalid document type"
            );
        }

        boolean validType =
                contentType.equals("application/pdf")
                        ||
                contentType.equals("image/jpeg")
                        ||
                contentType.equals("image/png")
                        ||
                contentType.equals("image/jpg");

        if (!validType) {
            throw new IllegalArgumentException(
                    "Only PDF, JPG, JPEG and PNG files are allowed"
            );
        }

        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "File size must be less than or equal to 10 MB"
            );
        }
    }
}

