package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ClientBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientBranchRepository extends JpaRepository<ClientBranch, Long> {

    List<ClientBranch> findByClientId(Long clientId);
}