package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.PsaraCompliance;
import com.security360.security360_backend.service.PsaraComplianceService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/psara")
@CrossOrigin(origins = {
        "http://localhost:8080",
        "http://localhost:8081"
})
public class PsaraController {

    private final PsaraComplianceService service;

    public PsaraController(
            PsaraComplianceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PsaraCompliance>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PsaraCompliance> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id));
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PsaraCompliance> create(
            @RequestPart("data")
            PsaraCompliance document,

            @RequestPart(
                    value = "certificate",
                    required = false)
            MultipartFile certificate)
            throws IOException {

        return ResponseEntity.status(
                HttpStatus.CREATED)
                .body(service.save(
                        document,
                        certificate));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PsaraCompliance> update(
            @PathVariable Long id,

            @RequestPart("data")
            PsaraCompliance document,

            @RequestPart(
                    value = "certificate",
                    required = false)
            MultipartFile certificate)
            throws IOException {

        return ResponseEntity.ok(
                service.update(
                        id,
                        document,
                        certificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id)
            throws IOException {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/certificate")
    public ResponseEntity<ByteArrayResource>
    downloadCertificate(
            @PathVariable Long id)
            throws IOException {

        PsaraCompliance document =
                service.getById(id);

        byte[] data =
                service.downloadCertificate(id);

        ByteArrayResource resource =
                new ByteArrayResource(data);

        String fileName =
                document.getCertificateFileName();

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        if (fileName != null &&
                fileName.toLowerCase()
                        .endsWith(".pdf")) {

            mediaType =
                    MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                (fileName == null
                                        ? "certificate"
                                        : fileName)
                                + "\"")
                .contentLength(data.length)
                .body(resource);
    }
}