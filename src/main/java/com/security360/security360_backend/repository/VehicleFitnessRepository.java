package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.VehicleFitness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleFitnessRepository
        extends JpaRepository<VehicleFitness, Long> {

    Optional<VehicleFitness> findByVehicleId(Long vehicleId);

    boolean existsByVehicleId(Long vehicleId);
}