package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.VehicleInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleInsuranceRepository
        extends JpaRepository<VehicleInsurance, Long> {

    Optional<VehicleInsurance> findByVehicleId(Long vehicleId);

    boolean existsByVehicleId(Long vehicleId);
}