package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.VendorPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorPaymentRepository extends JpaRepository<VendorPayment, Long> {

    // Fetch all payments sorted by newest first
    List<VendorPayment> findAllByOrderByCreatedAtDesc();
}