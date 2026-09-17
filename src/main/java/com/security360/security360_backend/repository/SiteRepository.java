package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    // 🟢 Used for Site Reports (Total Active/Inactive count)
    long countByStatus(String status);
}