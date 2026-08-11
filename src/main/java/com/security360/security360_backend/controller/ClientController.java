package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Client;
import com.security360.security360_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}