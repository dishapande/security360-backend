package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ClientContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientContractRepository extends JpaRepository<ClientContract, Long> {

    List<ClientContract> findByClientId(Long clientId);
}