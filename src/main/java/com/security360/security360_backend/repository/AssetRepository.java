package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findAllByOrderByCreatedAtDesc();
}