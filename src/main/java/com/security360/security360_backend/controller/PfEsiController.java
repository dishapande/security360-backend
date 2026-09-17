package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.PfEsiDashboardResponse;
import com.security360.security360_backend.entity.PfEsiRecord;
import com.security360.security360_backend.service.PfEsiService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pf-esi")
@CrossOrigin(
        origins = {
                "http://localhost:8080",
                "http://localhost:8081"
        }
)
public class PfEsiController {

    private final PfEsiService pfEsiService;

    public PfEsiController(PfEsiService pfEsiService) {
        this.pfEsiService = pfEsiService;
    }

    // ---------------------------------------------------------
    // DASHBOARD
    // GET /api/pf-esi
    // ---------------------------------------------------------

    @GetMapping
    public ResponseEntity<PfEsiDashboardResponse> getDashboard() {

        PfEsiDashboardResponse response =
                pfEsiService.getDashboard();

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // ALL RECORDS
    // GET /api/pf-esi/records
    // ---------------------------------------------------------

    @GetMapping("/records")
    public ResponseEntity<List<PfEsiRecord>> getAllRecords() {

        return ResponseEntity.ok(
                pfEsiService.getAll()
        );
    }

    // ---------------------------------------------------------
    // GET BY TYPE
    // GET /api/pf-esi/type/EMPLOYEE
    // ---------------------------------------------------------

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PfEsiRecord>> getByType(
            @PathVariable String type
    ) {

        return ResponseEntity.ok(
                pfEsiService.getByType(type)
        );
    }

    // ---------------------------------------------------------
    // GET BY STATUS
    // GET /api/pf-esi/status/PENDING
    // ---------------------------------------------------------

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PfEsiRecord>> getByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                pfEsiService.getByStatus(status)
        );
    }

    // ---------------------------------------------------------
    // GET SINGLE
    // GET /api/pf-esi/1
    // ---------------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ) {

        try {

            return ResponseEntity.ok(
                    pfEsiService.getById(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    // ---------------------------------------------------------
    // CREATE
    // POST /api/pf-esi
    // ---------------------------------------------------------

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody PfEsiRecord record
    ) {

        try {

            PfEsiRecord saved =
                    pfEsiService.create(record);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (IllegalArgumentException e) {

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

    // ---------------------------------------------------------
    // UPDATE
    // PUT /api/pf-esi/{id}
    // ---------------------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody PfEsiRecord record
    ) {

        try {

            return ResponseEntity.ok(
                    pfEsiService.update(id, record)
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

    // ---------------------------------------------------------
    // DELETE
    // DELETE /api/pf-esi/{id}
    // ---------------------------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {

        try {

            pfEsiService.delete(id);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "PF / ESI record deleted successfully"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}