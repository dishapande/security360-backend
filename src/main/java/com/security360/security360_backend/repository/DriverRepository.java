package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByVehicleId(Long vehicleId);

    List<Driver> findByStatus(String status);
}