package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {

    List<FuelRecord> findByVehicleIdOrderByFuelDateDesc(Long vehicleId);
}