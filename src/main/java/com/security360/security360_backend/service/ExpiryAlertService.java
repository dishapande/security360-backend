package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.ExpiryAlert;
import com.security360.security360_backend.repository.ExpiryAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpiryAlertService {

    private final ExpiryAlertRepository repository;

    public ExpiryAlertService(ExpiryAlertRepository repository) {
        this.repository = repository;
    }

    public List<ExpiryAlert> getAll() {

        List<ExpiryAlert> alerts = repository.findAll();

        for (ExpiryAlert alert : alerts) {
            alert.updateStatus();
        }

        return alerts;
    }

    public ExpiryAlert create(
            String type,
            String name,
            String reference,
            String owner,
            String site,
            String expiryDate,
            String renewalStatus,
            MultipartFile file
    ) throws IOException {

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Expiry type is required.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name is required.");
        }

        if (expiryDate == null || expiryDate.isBlank()) {
            throw new IllegalArgumentException("Expiry date is required.");
        }

        ExpiryAlert alert = new ExpiryAlert();

        alert.setType(type.trim().toUpperCase());
        alert.setName(name.trim());
        alert.setReference(clean(reference));
        alert.setOwner(clean(owner));
        alert.setSite(clean(site));

        alert.setExpiryDate(
                LocalDate.parse(expiryDate)
        );

        if (renewalStatus == null || renewalStatus.isBlank()) {
            alert.setRenewalStatus("PENDING");
        } else {
            alert.setRenewalStatus(
                    renewalStatus.trim().toUpperCase()
            );
        }

        if (file != null && !file.isEmpty()) {

            long maxFileSize = 10L * 1024L * 1024L;

            if (file.getSize() > maxFileSize) {
                throw new IllegalArgumentException(
                        "File size must not exceed 10 MB."
                );
            }

            String contentType = file.getContentType();

            if (!isAllowedFile(contentType, file.getOriginalFilename())) {
                throw new IllegalArgumentException(
                        "Only PDF, JPG, JPEG, PNG, DOC and DOCX files are allowed."
                );
            }

            alert.setFileName(
                    file.getOriginalFilename()
            );

            alert.setFileContentType(
                    contentType
            );

            alert.setFileData(
                    file.getBytes()
            );
        }

        alert.updateStatus();

        return repository.save(alert);
    }

    public ExpiryAlert getById(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Expiry record not found."
                        )
                );
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Expiry record not found."
            );
        }

        repository.deleteById(id);
    }

    private String clean(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private boolean isAllowedFile(
            String contentType,
            String fileName
    ) {

        if (fileName == null) {
            return false;
        }

        String lowerName =
                fileName.toLowerCase();

        boolean extensionAllowed =
                lowerName.endsWith(".pdf") ||
                lowerName.endsWith(".jpg") ||
                lowerName.endsWith(".jpeg") ||
                lowerName.endsWith(".png") ||
                lowerName.endsWith(".doc") ||
                lowerName.endsWith(".docx");

        if (!extensionAllowed) {
            return false;
        }

        if (contentType == null) {
            return true;
        }

        return contentType.equalsIgnoreCase("application/pdf")
                || contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("application/msword")
                || contentType.equalsIgnoreCase(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                );
    }
}