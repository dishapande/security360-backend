package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}