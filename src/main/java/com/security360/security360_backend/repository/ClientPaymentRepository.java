package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ClientPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Long> {

    List<ClientPayment> findByClientId(Long clientId);
}