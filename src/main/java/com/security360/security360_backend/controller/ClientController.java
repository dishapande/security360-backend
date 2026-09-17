package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Client;
import com.security360.security360_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:8081")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // 1. GET: Fetch all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    // 2. GET: Fetch single client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST: Create a new client
    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        try {
            clientRepository.save(client);
            return ResponseEntity.ok("Client created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating client: " + e.getMessage());
        }
    }

    // 4. PUT: Update an existing client
    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    // ✅ FIX: setName → setClientName / getName → getClientName
                    existingClient.setClientName(clientDetails.getClientName());
                    existingClient.setEmail(clientDetails.getEmail());
                    existingClient.setPhone(clientDetails.getPhone());
                    existingClient.setAddress(clientDetails.getAddress());
                    existingClient.setContactPerson(clientDetails.getContactPerson());
                    existingClient.setGstNo(clientDetails.getGstNo());
                    existingClient.setPanNo(clientDetails.getPanNo());
                    existingClient.setPaymentStatus(clientDetails.getPaymentStatus());
                    clientRepository.save(existingClient);
                    return ResponseEntity.ok("Client updated successfully!");
                })
                .orElse(ResponseEntity.badRequest().body("Client not found"));
    }

    // 5. DELETE: Delete a client
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return ResponseEntity.ok("Client deleted successfully!");
        }
        return ResponseEntity.badRequest().body("Client not found");
    }

    // 6. GET: Client Report (Basic stats - Original endpoint)
    @GetMapping("/report")
    public ResponseEntity<String> getClientReport() {
        long totalClients = clientRepository.count();
        long activeClients = clientRepository.countByPaymentStatus("Active");
        long pendingClients = clientRepository.countByPaymentStatus("Pending");
        long overdueClients = clientRepository.countByPaymentStatus("Overdue");

        return ResponseEntity.ok(
            "{\"totalClients\": " + totalClients +
            ", \"activeClients\": " + activeClients +
            ", \"pendingClients\": " + pendingClients +
            ", \"overdueClients\": " + overdueClients + "}"
        );
    }

    // 7. GET: Full Report Data for Client Reports Page
    @GetMapping("/report-data")
    public ResponseEntity<Map<String, Object>> getClientReportData() {

        List<Client> clients = clientRepository.findAll();

        long totalClients = clients.size();
        long activeClients = clients.stream()
                .filter(c -> "Active".equalsIgnoreCase(c.getPaymentStatus()))
                .count();
        long pendingClients = clients.stream()
                .filter(c -> "Pending".equalsIgnoreCase(c.getPaymentStatus()))
                .count();
        long overdueClients = clients.stream()
                .filter(c -> "Overdue".equalsIgnoreCase(c.getPaymentStatus()))
                .count();
        long inactiveClients = clients.stream()
                .filter(c -> "Inactive".equalsIgnoreCase(c.getPaymentStatus()))
                .count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalClients", totalClients);
        response.put("activeClients", activeClients);
        response.put("pendingClients", pendingClients);
        response.put("overdueClients", overdueClients);
        response.put("inactiveClients", inactiveClients);
        response.put("clients", clients);

        return ResponseEntity.ok(response);
    }
}