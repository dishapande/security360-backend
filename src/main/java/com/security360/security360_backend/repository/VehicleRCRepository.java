package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.VehicleRC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRCRepository extends JpaRepository<VehicleRC, Long> {

    Optional<VehicleRC> findByVehicleId(Long vehicleId);

    boolean existsByVehicleId(Long vehicleId);
}