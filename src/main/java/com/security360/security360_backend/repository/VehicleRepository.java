package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByClientId(Long clientId);

    List<Vehicle> findByStatus(String status);
}