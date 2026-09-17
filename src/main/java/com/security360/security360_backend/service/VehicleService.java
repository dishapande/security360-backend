package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Vehicle;
import com.security360.security360_backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found with id: " + id));
    }

    public Vehicle createVehicle(Vehicle vehicle) {

        if (vehicle.getVehicleNumber() == null ||
                vehicle.getVehicleNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number is required"
            );
        }

        String vehicleNumber =
                vehicle.getVehicleNumber().trim().toUpperCase();

        if (vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
            throw new IllegalArgumentException(
                    "Vehicle number already exists: " + vehicleNumber
            );
        }

        vehicle.setVehicleNumber(vehicleNumber);

        if (vehicle.getStatus() == null ||
                vehicle.getStatus().isBlank()) {
            vehicle.setStatus("ACTIVE");
        }

        if (vehicle.getGpsEnabled() == null) {
            vehicle.setGpsEnabled(false);
        }

        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {

        Vehicle existingVehicle = getVehicleById(id);

        if (updatedVehicle.getVehicleNumber() != null &&
                !updatedVehicle.getVehicleNumber().trim().isEmpty()) {

            String newNumber =
                    updatedVehicle.getVehicleNumber()
                            .trim()
                            .toUpperCase();

            if (!existingVehicle.getVehicleNumber()
                    .equalsIgnoreCase(newNumber)
                    && vehicleRepository.existsByVehicleNumber(newNumber)) {

                throw new IllegalArgumentException(
                        "Vehicle number already exists: " + newNumber
                );
            }

            existingVehicle.setVehicleNumber(newNumber);
        }

        if (updatedVehicle.getVehicleType() != null) {
            existingVehicle.setVehicleType(
                    updatedVehicle.getVehicleType()
            );
        }

        if (updatedVehicle.getMake() != null) {
            existingVehicle.setMake(updatedVehicle.getMake());
        }

        if (updatedVehicle.getModel() != null) {
            existingVehicle.setModel(updatedVehicle.getModel());
        }

        if (updatedVehicle.getManufacturingYear() != null) {
            existingVehicle.setManufacturingYear(
                    updatedVehicle.getManufacturingYear()
            );
        }

        if (updatedVehicle.getColor() != null) {
            existingVehicle.setColor(updatedVehicle.getColor());
        }

        if (updatedVehicle.getDriverName() != null) {
            existingVehicle.setDriverName(
                    updatedVehicle.getDriverName()
            );
        }

        if (updatedVehicle.getSite() != null) {
            existingVehicle.setSite(updatedVehicle.getSite());
        }

        if (updatedVehicle.getFastagNumber() != null) {
            existingVehicle.setFastagNumber(
                    updatedVehicle.getFastagNumber()
            );
        }

        if (updatedVehicle.getGpsEnabled() != null) {
            existingVehicle.setGpsEnabled(
                    updatedVehicle.getGpsEnabled()
            );
        }

        if (updatedVehicle.getStatus() != null) {
            existingVehicle.setStatus(
                    updatedVehicle.getStatus()
            );
        }

        if (updatedVehicle.getClientId() != null) {
            existingVehicle.setClientId(
                    updatedVehicle.getClientId()
            );
        }

        return vehicleRepository.save(existingVehicle);
    }

    public void deleteVehicle(Long id) {

        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException(
                    "Vehicle not found with id: " + id
            );
        }

        vehicleRepository.deleteById(id);
    }

    public List<Vehicle> getVehiclesByClient(Long clientId) {
        return vehicleRepository.findByClientId(clientId);
    }

    public List<Vehicle> getVehiclesByStatus(String status) {
        return vehicleRepository.findByStatus(status);
    }
}