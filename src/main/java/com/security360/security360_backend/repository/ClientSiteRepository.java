package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.ClientSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientSiteRepository extends JpaRepository<ClientSite, Long> {

    List<ClientSite> findByClientId(Long clientId);
}