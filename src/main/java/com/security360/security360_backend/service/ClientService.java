package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Client;
import com.security360.security360_backend.repository.ClientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // =====================================================
    // CLIENTS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found with id: " + id)
                );
    }

    public Client createClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client data is required");
        }

        if (isBlank(client.getClientName())) {
            throw new IllegalArgumentException("Client name is required");
        }

        if (isBlank(client.getContactPerson())) {
            throw new IllegalArgumentException("Contact person is required");
        }

        client.setClientName(client.getClientName().trim());
        client.setContactPerson(client.getContactPerson().trim());

        if (client.getPaymentStatus() == null || client.getPaymentStatus().isBlank()) {
            client.setPaymentStatus("Active");
        }

        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client updatedClient) {
        if (updatedClient == null) {
            throw new IllegalArgumentException("Client data is required");
        }

        Client existing = getClient(id);

        if (!isBlank(updatedClient.getClientName())) {
            existing.setClientName(updatedClient.getClientName().trim());
        }

        if (!isBlank(updatedClient.getContactPerson())) {
            existing.setContactPerson(updatedClient.getContactPerson().trim());
        }

        existing.setEmail(updatedClient.getEmail());
        existing.setPhone(updatedClient.getPhone());
        existing.setAddress(updatedClient.getAddress());
        existing.setGstNo(updatedClient.getGstNo());
        existing.setPanNo(updatedClient.getPanNo());

        if (!isBlank(updatedClient.getPaymentStatus())) {
            existing.setPaymentStatus(updatedClient.getPaymentStatus().trim());
        }

        return clientRepository.save(existing);
    }

    public void deleteClient(Long id) {
        Client client = getClient(id);
        clientRepository.delete(client);
    }

    // =====================================================
    // REPORT
    // =====================================================

    @Transactional(readOnly = true)
    public ClientReport getReport() {
        List<Client> clients = clientRepository.findAll();

        long totalClients = clients.size();
        long activeClients = clients.stream()
                .filter(c -> "Active".equalsIgnoreCase(safe(c.getPaymentStatus())))
                .count();

        return new ClientReport(totalClients, activeClients);
    }

    // =====================================================
    // HELPERS
    // =====================================================

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    // =====================================================
    // REPORT DTO
    // =====================================================

    public static class ClientReport {
        private final long totalClients;
        private final long activeClients;

        public ClientReport(long totalClients, long activeClients) {
            this.totalClients = totalClients;
            this.activeClients = activeClients;
        }

        public long getTotalClients() { return totalClients; }
        public long getActiveClients() { return activeClients; }
    }
}