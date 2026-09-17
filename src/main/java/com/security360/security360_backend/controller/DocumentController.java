package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Document;
import com.security360.security360_backend.service.DocumentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = {
        
        "http://localhost:8081"
})
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(
                documentService.getAllDocuments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                documentService.getDocumentById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestBody Document document
    ) {
        return ResponseEntity.ok(
                documentService.saveDocument(document)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestBody Document document
    ) {
        Document existing =
                documentService.getDocumentById(id);

        existing.setTitle(document.getTitle());
        existing.setOwner(document.getOwner());
        existing.setCategory(document.getCategory());
        existing.setDocumentType(document.getDocumentType());
        existing.setExpiryDate(document.getExpiryDate());
        existing.setStatus(document.getStatus());
        existing.setDescription(document.getDescription());
        existing.setFileName(document.getFileName());
        existing.setFilePath(document.getFilePath());

        return ResponseEntity.ok(
                documentService.saveDocument(existing)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id
    ) {
        documentService.deleteDocument(id);

        return ResponseEntity.noContent().build();
    }
}