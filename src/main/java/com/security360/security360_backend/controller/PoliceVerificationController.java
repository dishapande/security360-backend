package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.PoliceVerification;
import com.security360.security360_backend.service.PoliceVerificationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/police-verifications")
@CrossOrigin(origins = {
        "http://localhost:8080",
        "http://localhost:8081"
})
public class PoliceVerificationController {

    private final PoliceVerificationService service;

    public PoliceVerificationController(
            PoliceVerificationService service
    ) {
        this.service = service;
    }

    /*
     * GET ALL
     * GET /api/police-verifications
     */
    @GetMapping
    public ResponseEntity<List<PoliceVerification>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }

    /*
     * GET BY ID
     * GET /api/police-verifications/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<PoliceVerification> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }

    /*
     * SEARCH
     * GET /api/police-verifications/search?keyword=ABC
     */
    @GetMapping("/search")
    public ResponseEntity<List<PoliceVerification>> search(
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(
                service.search(keyword)
        );
    }

    /*
     * FILTER BY STATUS
     * GET /api/police-verifications/status/Verified
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PoliceVerification>> getByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                service.getByStatus(status)
        );
    }

    /*
     * CREATE
     * POST /api/police-verifications
     *
     * multipart/form-data
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PoliceVerification> create(

            @RequestPart("data")
            PoliceVerification verification,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file

    ) throws Exception {

        PoliceVerification saved =
                service.create(
                        verification,
                        file
                );

        return ResponseEntity.ok(saved);
    }

    /*
     * UPDATE
     * PUT /api/police-verifications/{id}
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PoliceVerification> update(

            @PathVariable Long id,

            @RequestPart("data")
            PoliceVerification verification,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file

    ) throws Exception {

        PoliceVerification updated =
                service.update(
                        id,
                        verification,
                        file
                );

        return ResponseEntity.ok(updated);
    }

    /*
     * DELETE
     * DELETE /api/police-verifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * VIEW DOCUMENT
     * GET /api/police-verifications/{id}/document
     */
    @GetMapping("/{id}/document")
    public ResponseEntity<ByteArrayResource> viewDocument(
            @PathVariable Long id
    ) {

        byte[] data =
                service.getDocument(id);

        String contentType =
                service.getDocumentContentType(id);

        String fileName =
                service.getDocumentFileName(id);

        ByteArrayResource resource =
                new ByteArrayResource(data);

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(
                    contentType
            );
        } catch (Exception e) {
            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                fileName +
                                "\""
                )
                .contentLength(data.length)
                .body(resource);
    }

    /*
     * DOWNLOAD DOCUMENT
     * GET /api/police-verifications/{id}/document/download
     */
    @GetMapping("/{id}/document/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(
            @PathVariable Long id
    ) {

        byte[] data =
                service.getDocument(id);

        String contentType =
                service.getDocumentContentType(id);

        String fileName =
                service.getDocumentFileName(id);

        ByteArrayResource resource =
                new ByteArrayResource(data);

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(
                    contentType
            );
        } catch (Exception e) {
            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileName +
                                "\""
                )
                .contentLength(data.length)
                .body(resource);
    }
}
