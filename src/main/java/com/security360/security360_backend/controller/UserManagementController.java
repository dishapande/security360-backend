package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.UserManagementRequest;
import com.security360.security360_backend.entity.ManagedUser;
import com.security360.security360_backend.service.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
        "http://localhost:8080",
        "http://localhost:8081"
})
public class UserManagementController {

    private final UserManagementService service;

    public UserManagementController(
            UserManagementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ManagedUser>> getUsers() {

        return ResponseEntity.ok(
                service.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagedUser> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getUser(id)
        );
    }

    @PostMapping
    public ResponseEntity<ManagedUser> createUser(
            @RequestBody UserManagementRequest request) {

        return ResponseEntity.ok(
                service.createUser(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagedUser> updateUser(
            @PathVariable Long id,
            @RequestBody UserManagementRequest request) {

        return ResponseEntity.ok(
                service.updateUser(id, request)
        );
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ManagedUser> assignRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                service.assignRole(
                        id,
                        body.get("role")
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ManagedUser> changeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                service.changeStatus(
                        id,
                        body.get("status")
                )
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ManagedUser> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.activate(id)
        );
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ManagedUser> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deactivate(id)
        );
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<ManagedUser> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                service.resetPassword(
                        id,
                        body.get("password")
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk/activate")
    public ResponseEntity<Void> bulkActivate(
            @RequestBody List<Long> ids) {

        service.bulkActivate(ids);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk/deactivate")
    public ResponseEntity<Void> bulkDeactivate(
            @RequestBody List<Long> ids) {

        service.bulkDeactivate(ids);

        return ResponseEntity.ok().build();
    }
}