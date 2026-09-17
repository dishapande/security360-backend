package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.GstInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GstInvoiceRepository
        extends JpaRepository<GstInvoice, Long> {

    List<GstInvoice> findByStatusIgnoreCase(String status);

    List<GstInvoice> findByPartyContainingIgnoreCase(String party);

    boolean existsByReferenceNo(String referenceNo);
}