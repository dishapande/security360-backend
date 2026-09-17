package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}