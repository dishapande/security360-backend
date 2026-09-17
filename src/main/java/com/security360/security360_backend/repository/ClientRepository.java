package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    long countByPaymentStatus(String paymentStatus);

}