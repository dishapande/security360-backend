package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.PsaraCompliance;
import com.security360.security360_backend.repository.PsaraComplianceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class PsaraComplianceService {

    private final PsaraComplianceRepository repository;

    private final Path uploadDirectory =
            Paths.get("uploads/psara");

    public PsaraComplianceService(
            PsaraComplianceRepository repository) {
        this.repository = repository;

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory", e);
        }
    }

    public List<PsaraCompliance> getAll() {
        return repository.findAll();
    }

    public PsaraCompliance getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PSARA document not found"));
    }

    public PsaraCompliance save(
            PsaraCompliance document,
            MultipartFile certificate) throws IOException {

        if (certificate != null &&
                !certificate.isEmpty()) {

            String originalName =
                    certificate.getOriginalFilename();

            String safeName =
                    System.currentTimeMillis()
                            + "_"
                            + (originalName == null
                            ? "certificate"
                            : originalName.replaceAll(
                                    "[^a-zA-Z0-9._-]",
                                    "_"));

            Path filePath =
                    uploadDirectory.resolve(safeName);

            Files.copy(
                    certificate.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            document.setCertificateFileName(originalName);
            document.setCertificateFilePath(
                    filePath.toString());
        }

        return repository.save(document);
    }

    public PsaraCompliance update(
            Long id,
            PsaraCompliance updated,
            MultipartFile certificate)
            throws IOException {

        PsaraCompliance existing =
                getById(id);

        existing.setTitle(updated.getTitle());
        existing.setLicenseNumber(
                updated.getLicenseNumber());
        existing.setState(updated.getState());
        existing.setOwner(updated.getOwner());
        existing.setType(updated.getType());
        existing.setIssuingAuthority(
                updated.getIssuingAuthority());
        existing.setIssueDate(
                updated.getIssueDate());
        existing.setExpiryDate(
                updated.getExpiryDate());
        existing.setRenewalDate(
                updated.getRenewalDate());
        existing.setStatus(
                updated.getStatus());
        existing.setNotes(
                updated.getNotes());

        if (certificate != null &&
                !certificate.isEmpty()) {

            String originalName =
                    certificate.getOriginalFilename();

            String safeName =
                    System.currentTimeMillis()
                            + "_"
                            + (originalName == null
                            ? "certificate"
                            : originalName.replaceAll(
                                    "[^a-zA-Z0-9._-]",
                                    "_"));

            Path filePath =
                    uploadDirectory.resolve(safeName);

            Files.copy(
                    certificate.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            existing.setCertificateFileName(
                    originalName);

            existing.setCertificateFilePath(
                    filePath.toString());
        }

        return repository.save(existing);
    }

    public void delete(Long id) throws IOException {

        PsaraCompliance document =
                getById(id);

        if (document.getCertificateFilePath() != null) {

            try {
                Files.deleteIfExists(
                        Paths.get(
                                document
                                        .getCertificateFilePath()));
            } catch (Exception ignored) {
            }
        }

        repository.deleteById(id);
    }

    public byte[] downloadCertificate(Long id)
            throws IOException {

        PsaraCompliance document =
                getById(id);

        if (document.getCertificateFilePath() == null) {
            throw new RuntimeException(
                    "Certificate not uploaded");
        }

        Path path = Paths.get(
                document.getCertificateFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException(
                    "Certificate file not found");
        }

        return Files.readAllBytes(path);
    }
}