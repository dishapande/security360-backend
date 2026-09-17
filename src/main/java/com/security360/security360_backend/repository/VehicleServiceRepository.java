package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.VehicleService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleServiceRepository
        extends JpaRepository<VehicleService, Long> {

    List<VehicleService> findByVehicleIdOrderByServiceDateDesc(Long vehicleId);
}