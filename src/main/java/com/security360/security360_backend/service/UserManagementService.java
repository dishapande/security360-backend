package com.security360.security360_backend.service;

import com.security360.security360_backend.dto.UserManagementRequest;
import com.security360.security360_backend.entity.ManagedUser;
import com.security360.security360_backend.repository.ManagedUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserManagementService {

    private final ManagedUserRepository repository;

    public UserManagementService(
            ManagedUserRepository repository) {
        this.repository = repository;
    }

    public List<ManagedUser> getAllUsers() {
        return repository.findAll();
    }

    public ManagedUser getUser(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public ManagedUser createUser(
            UserManagementRequest request) {

        validateRequest(request);

        if (repository.existsByEmailIgnoreCase(
                request.getEmail().trim())) {

            throw new RuntimeException(
                    "Email already exists");
        }

        ManagedUser user = new ManagedUser();

        user.setUserCode(generateUserCode());
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setRole(request.getRole());
        user.setBranch(request.getBranch().trim());
        user.setStatus(
                request.getStatus() == null ||
                request.getStatus().isBlank()
                        ? "Active"
                        : request.getStatus()
        );

        user.setPassword(
                request.getPassword() == null ||
                request.getPassword().isBlank()
                        ? "ChangeMe123"
                        : request.getPassword()
        );

        user.setLastActive(null);

        return repository.save(user);
    }

    public ManagedUser updateUser(
            Long id,
            UserManagementRequest request) {

        ManagedUser user = getUser(id);

        validateRequest(request);

        repository.findByEmailIgnoreCase(
                request.getEmail().trim()
        ).ifPresent(existing -> {

            if (!existing.getId().equals(id)) {
                throw new RuntimeException(
                        "Email already exists");
            }
        });

        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setRole(request.getRole());
        user.setBranch(request.getBranch().trim());

        if (request.getStatus() != null &&
                !request.getStatus().isBlank()) {

            user.setStatus(request.getStatus());
        }

        return repository.save(user);
    }

    public ManagedUser assignRole(
            Long id,
            String role) {

        if (role == null || role.isBlank()) {
            throw new RuntimeException(
                    "Role is required");
        }

        ManagedUser user = getUser(id);

        user.setRole(role);

        return repository.save(user);
    }

    public ManagedUser changeStatus(
            Long id,
            String status) {

        if (status == null || status.isBlank()) {
            throw new RuntimeException(
                    "Status is required");
        }

        ManagedUser user = getUser(id);

        user.setStatus(status);

        return repository.save(user);
    }

    public ManagedUser activate(Long id) {
        return changeStatus(id, "Active");
    }

    public ManagedUser deactivate(Long id) {
        return changeStatus(id, "Inactive");
    }

    public ManagedUser resetPassword(
            Long id,
            String password) {

        if (password == null ||
                password.length() < 6) {

            throw new RuntimeException(
                    "Password must contain at least 6 characters");
        }

        ManagedUser user = getUser(id);

        user.setPassword(password);

        return repository.save(user);
    }

    public void deleteUser(Long id) {

        ManagedUser user = getUser(id);

        repository.delete(user);
    }

    public void bulkActivate(
            List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<ManagedUser> users =
                repository.findAllById(ids);

        users.forEach(user ->
                user.setStatus("Active"));

        repository.saveAll(users);
    }

    public void bulkDeactivate(
            List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<ManagedUser> users =
                repository.findAllById(ids);

        users.forEach(user ->
                user.setStatus("Inactive"));

        repository.saveAll(users);
    }

    private String generateUserCode() {

        long nextNumber =
                1000L + repository.count() + 1;

        String code =
                "U-" + nextNumber;

        while (repository.findByUserCode(code).isPresent()) {
            nextNumber++;
            code = "U-" + nextNumber;
        }

        return code;
    }

    private void validateRequest(
            UserManagementRequest request) {

        if (request == null) {
            throw new RuntimeException(
                    "Request cannot be empty");
        }

        if (request.getName() == null ||
                request.getName().isBlank()) {

            throw new RuntimeException(
                    "Name is required");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank() ||
                !request.getEmail().contains("@")) {

            throw new RuntimeException(
                    "Valid email is required");
        }

        if (request.getRole() == null ||
                request.getRole().isBlank()) {

            throw new RuntimeException(
                    "Role is required");
        }

        if (request.getBranch() == null ||
                request.getBranch().isBlank()) {

            throw new RuntimeException(
                    "Branch is required");
        }
    }
}