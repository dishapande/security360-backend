package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByVehicleIdOrderByStartTimeDesc(Long vehicleId);

    List<Trip> findByDriverIdOrderByStartTimeDesc(Long driverId);
}