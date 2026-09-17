package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Visitor;
import com.security360.security360_backend.entity.Visitor.VisitorStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByVisitorId(String visitorId);

    Optional<Visitor> findByPassId(String passId);

    List<Visitor> findByStatus(VisitorStatus status);

    List<Visitor> findByNameContainingIgnoreCase(String name);

    // Today's visitors
    List<Visitor> findByVisitDate(LocalDate visitDate);

    long countByStatus(VisitorStatus status);
}