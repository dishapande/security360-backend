package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.ExpiryAlert;
import com.security360.security360_backend.service.ExpiryAlertService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expiry-alerts")
@CrossOrigin(
        origins = {
                "http://localhost:8080",
                "http://localhost:8081"
        }
)
public class ExpiryAlertController {

    private final ExpiryAlertService service;

    public ExpiryAlertController(
            ExpiryAlertService service
    ) {
        this.service = service;
    }

    /*
     * GET /api/expiry-alerts
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {

        List<Map<String, Object>> result =
                service.getAll()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /*
     * POST /api/expiry-alerts
     *
     * multipart/form-data
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String site,
            @RequestParam String expiryDate,
            @RequestParam(required = false) String renewalStatus,
            @RequestPart(required = false) MultipartFile file
    ) {

        try {

            ExpiryAlert saved =
                    service.create(
                            type,
                            name,
                            reference,
                            owner,
                            site,
                            expiryDate,
                            renewalStatus,
                            file
                    );

            return ResponseEntity.ok(
                    toResponse(saved)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(
                            errorResponse(
                                    e.getMessage()
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(
                            errorResponse(
                                    "Unable to save expiry record."
                            )
                    );
        }
    }

    /*
     * GET /api/expiry-alerts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ) {

        try {

            return ResponseEntity.ok(
                    toResponse(
                            service.getById(id)
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound()
                    .build();
        }
    }

    /*
     * GET /api/expiry-alerts/{id}/file
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<?> downloadFile(
            @PathVariable Long id
    ) {

        try {

            ExpiryAlert alert =
                    service.getById(id);

            if (
                    alert.getFileData() == null ||
                    alert.getFileData().length == 0
            ) {
                return ResponseEntity.notFound()
                        .build();
            }

            String contentType =
                    alert.getFileContentType();

            MediaType mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;

            if (contentType != null) {
                try {
                    mediaType =
                            MediaType.parseMediaType(
                                    contentType
                            );
                } catch (Exception ignored) {
                }
            }

            String fileName =
                    alert.getFileName() != null
                            ? alert.getFileName()
                            : "expiry-document";

            ByteArrayResource resource =
                    new ByteArrayResource(
                            alert.getFileData()
                    );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(mediaType);

            headers.setContentDisposition(
                    ContentDisposition
                            .attachment()
                            .filename(fileName)
                            .build()
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(
                            alert.getFileData().length
                    )
                    .body(resource);

        } catch (RuntimeException e) {

            return ResponseEntity.notFound()
                    .build();
        }
    }

    /*
     * DELETE /api/expiry-alerts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id
    ) {

        try {

            service.delete(id);

            Map<String, String> response =
                    new LinkedHashMap<>();

            response.put(
                    "message",
                    "Expiry record deleted successfully."
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity.notFound()
                    .build();
        }
    }

    private Map<String, Object> toResponse(
            ExpiryAlert alert
    ) {

        LocalDate expiry =
                alert.getExpiryDate();

        long daysRemaining =
                ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        expiry
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "id",
                alert.getId()
        );

        response.put(
                "alertId",
                "EXP-" + alert.getId()
        );

        response.put(
                "type",
                alert.getType()
        );

        response.put(
                "name",
                alert.getName()
        );

        response.put(
                "reference",
                alert.getReference()
        );

        response.put(
                "owner",
                alert.getOwner()
        );

        response.put(
                "site",
                alert.getSite()
        );

        response.put(
                "expiryDate",
                alert.getExpiryDate()
        );

        response.put(
                "daysRemaining",
                daysRemaining
        );

        response.put(
                "status",
                alert.getStatus()
        );

        response.put(
                "renewalStatus",
                alert.getRenewalStatus()
        );

        response.put(
                "fileName",
                alert.getFileName()
        );

        response.put(
                "fileAvailable",
                alert.getFileData() != null &&
                        alert.getFileData().length > 0
        );

        response.put(
                "fileUrl",
                alert.getFileData() != null &&
                        alert.getFileData().length > 0
                        ? "/api/expiry-alerts/"
                                + alert.getId()
                                + "/file"
                        : null
        );

        response.put(
                "createdAt",
                alert.getCreatedAt()
        );

        return response;
    }

    private Map<String, Object> errorResponse(
            String message
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "message",
                message
        );

        return response;
    }
}