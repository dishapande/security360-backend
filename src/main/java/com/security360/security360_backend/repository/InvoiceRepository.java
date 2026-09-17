package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}