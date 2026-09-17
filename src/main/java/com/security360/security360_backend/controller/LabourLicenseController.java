package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.LabourLicense;
import com.security360.security360_backend.service.LabourLicenseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/labour-licenses")
@CrossOrigin(
        origins = {
                "http://localhost:8080",
                "http://localhost:8081"
        }
)
public class LabourLicenseController {

    private final LabourLicenseService service;

    public LabourLicenseController(
            LabourLicenseService service
    ) {
        this.service = service;
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<LabourLicense>> getAllLicenses() {

        return ResponseEntity.ok(
                service.getAllLicenses()
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<LabourLicense> getLicenseById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getLicenseById(id)
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<?> createLicense(
            @RequestBody LabourLicense license
    ) {

        try {

            LabourLicense saved =
                    service.createLicense(license);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLicense(
            @PathVariable Long id,
            @RequestBody LabourLicense license
    ) {

        try {

            LabourLicense updated =
                    service.updateLicense(id, license);

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    // =====================================================
    // RENEW
    // =====================================================

    @PutMapping("/{id}/renew")
    public ResponseEntity<?> renewLicense(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {

        try {

            String renewalDateString =
                    request.get("renewalDate");

            String newExpiryDateString =
                    request.get("newExpiryDate");

            if (renewalDateString == null ||
                    newExpiryDateString == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Renewal Date and New Expiry Date are required"
                                )
                        );
            }

            LocalDate renewalDate =
                    LocalDate.parse(renewalDateString);

            LocalDate newExpiryDate =
                    LocalDate.parse(newExpiryDateString);

            LabourLicense renewed =
                    service.renewLicense(
                            id,
                            renewalDate,
                            newExpiryDate
                    );

            return ResponseEntity.ok(renewed);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLicense(
            @PathVariable Long id
    ) {

        try {

            service.deleteLicense(id);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Labour License deleted successfully"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}