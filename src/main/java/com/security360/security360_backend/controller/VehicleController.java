package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Driver;
import com.security360.security360_backend.entity.FuelRecord;
import com.security360.security360_backend.entity.Trip;
import com.security360.security360_backend.entity.Vehicle;
import com.security360.security360_backend.entity.VehicleFitness;
import com.security360.security360_backend.entity.VehicleInsurance;
import com.security360.security360_backend.entity.VehicleRC;

import com.security360.security360_backend.repository.DriverRepository;
import com.security360.security360_backend.repository.FuelRecordRepository;
import com.security360.security360_backend.repository.TripRepository;
import com.security360.security360_backend.repository.VehicleFitnessRepository;
import com.security360.security360_backend.repository.VehicleInsuranceRepository;
import com.security360.security360_backend.repository.VehicleRCRepository;
import com.security360.security360_backend.repository.VehicleServiceRepository;

import com.security360.security360_backend.service.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = {
        
        "http://localhost:8081"
})
public class VehicleController {

    private final VehicleService vehicleService;
    private final DriverRepository driverRepository;
    private final FuelRecordRepository fuelRecordRepository;
    private final VehicleInsuranceRepository insuranceRepository;
    private final VehicleRCRepository rcRepository;
    private final VehicleFitnessRepository fitnessRepository;
    private final VehicleServiceRepository serviceRepository;
    private final TripRepository tripRepository;

    public VehicleController(
            VehicleService vehicleService,
            DriverRepository driverRepository,
            FuelRecordRepository fuelRecordRepository,
            VehicleInsuranceRepository insuranceRepository,
            VehicleRCRepository rcRepository,
            VehicleFitnessRepository fitnessRepository,
            VehicleServiceRepository serviceRepository,
            TripRepository tripRepository
    ) {
        this.vehicleService = vehicleService;
        this.driverRepository = driverRepository;
        this.fuelRecordRepository = fuelRecordRepository;
        this.insuranceRepository = insuranceRepository;
        this.rcRepository = rcRepository;
        this.fitnessRepository = fitnessRepository;
        this.serviceRepository = serviceRepository;
        this.tripRepository = tripRepository;
    }

    // =========================================================
    // VEHICLE
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(
                vehicleService.getAllVehicles()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicle(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                vehicleService.getVehicleById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(
            @RequestBody Vehicle vehicle
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.createVehicle(vehicle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle
    ) {
        return ResponseEntity.ok(
                vehicleService.updateVehicle(id, vehicle)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id
    ) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByClient(
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(
                vehicleService.getVehiclesByClient(clientId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vehicle>> getVehiclesByStatus(
            @PathVariable String status
    ) {
        return ResponseEntity.ok(
                vehicleService.getVehiclesByStatus(status)
        );
    }

    // =========================================================
    // DRIVER
    // =========================================================

    @GetMapping("/{vehicleId}/drivers")
    public ResponseEntity<List<Driver>> getDrivers(
            @PathVariable Long vehicleId
    ) {
        return ResponseEntity.ok(
                driverRepository.findByVehicleId(vehicleId)
        );
    }

    @PostMapping("/{vehicleId}/drivers")
    public ResponseEntity<Driver> addDriver(
            @PathVariable Long vehicleId,
            @RequestBody Driver driver
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        driver.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(driverRepository.save(driver));
    }

    @PutMapping("/{vehicleId}/drivers/{driverId}")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable Long vehicleId,
            @PathVariable Long driverId,
            @RequestBody Driver updatedDriver
    ) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found")
                );

        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        driver.setVehicle(vehicle);

        if (updatedDriver.getDriverName() != null) {
            driver.setDriverName(
                    updatedDriver.getDriverName()
            );
        }

        if (updatedDriver.getPhone() != null) {
            driver.setPhone(
                    updatedDriver.getPhone()
            );
        }

        if (updatedDriver.getLicenseNumber() != null) {
            driver.setLicenseNumber(
                    updatedDriver.getLicenseNumber()
            );
        }

        if (updatedDriver.getLicenseExpiry() != null) {
            driver.setLicenseExpiry(
                    updatedDriver.getLicenseExpiry()
            );
        }

        if (updatedDriver.getAddress() != null) {
            driver.setAddress(
                    updatedDriver.getAddress()
            );
        }

        if (updatedDriver.getStatus() != null) {
            driver.setStatus(
                    updatedDriver.getStatus()
            );
        }

        return ResponseEntity.ok(
                driverRepository.save(driver)
        );
    }

    @DeleteMapping("/{vehicleId}/drivers/{driverId}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable Long vehicleId,
            @PathVariable Long driverId
    ) {
        if (!driverRepository.existsById(driverId)) {
            throw new RuntimeException("Driver not found");
        }

        driverRepository.deleteById(driverId);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // FUEL
    // =========================================================

    @GetMapping("/{vehicleId}/fuel")
    public ResponseEntity<List<FuelRecord>> getFuelRecords(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return ResponseEntity.ok(
                fuelRecordRepository
                        .findByVehicleIdOrderByFuelDateDesc(vehicleId)
        );
    }

    @PostMapping("/{vehicleId}/fuel")
    public ResponseEntity<FuelRecord> addFuel(
            @PathVariable Long vehicleId,
            @RequestBody FuelRecord fuelRecord
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        fuelRecord.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fuelRecordRepository.save(fuelRecord));
    }

    @DeleteMapping("/{vehicleId}/fuel/{fuelId}")
    public ResponseEntity<Void> deleteFuel(
            @PathVariable Long vehicleId,
            @PathVariable Long fuelId
    ) {
        if (!fuelRecordRepository.existsById(fuelId)) {
            throw new RuntimeException("Fuel record not found");
        }

        fuelRecordRepository.deleteById(fuelId);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // INSURANCE
    // =========================================================

    @GetMapping("/{vehicleId}/insurance")
    public ResponseEntity<VehicleInsurance> getInsurance(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return insuranceRepository
                .findByVehicleId(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PostMapping("/{vehicleId}/insurance")
    public ResponseEntity<VehicleInsurance> addInsurance(
            @PathVariable Long vehicleId,
            @RequestBody VehicleInsurance insurance
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        if (insuranceRepository.existsByVehicleId(vehicleId)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        insurance.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insuranceRepository.save(insurance));
    }

    @PutMapping("/{vehicleId}/insurance")
    public ResponseEntity<VehicleInsurance> updateInsurance(
            @PathVariable Long vehicleId,
            @RequestBody VehicleInsurance updated
    ) {
        VehicleInsurance insurance =
                insuranceRepository.findByVehicleId(vehicleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Insurance not found"
                                )
                        );

        if (updated.getPolicyNumber() != null) {
            insurance.setPolicyNumber(
                    updated.getPolicyNumber()
            );
        }

        if (updated.getInsuranceCompany() != null) {
            insurance.setInsuranceCompany(
                    updated.getInsuranceCompany()
            );
        }

        if (updated.getStartDate() != null) {
            insurance.setStartDate(
                    updated.getStartDate()
            );
        }

        if (updated.getExpiryDate() != null) {
            insurance.setExpiryDate(
                    updated.getExpiryDate()
            );
        }

        if (updated.getPremium() != null) {
            insurance.setPremium(
                    updated.getPremium()
            );
        }

        if (updated.getStatus() != null) {
            insurance.setStatus(
                    updated.getStatus()
            );
        }

        return ResponseEntity.ok(
                insuranceRepository.save(insurance)
        );
    }

    // =========================================================
    // RC
    // =========================================================

    @GetMapping("/{vehicleId}/rc")
    public ResponseEntity<VehicleRC> getRC(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return rcRepository
                .findByVehicleId(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PostMapping("/{vehicleId}/rc")
    public ResponseEntity<VehicleRC> addRC(
            @PathVariable Long vehicleId,
            @RequestBody VehicleRC rc
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        if (rcRepository.existsByVehicleId(vehicleId)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        rc.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rcRepository.save(rc));
    }

    @PutMapping("/{vehicleId}/rc")
    public ResponseEntity<VehicleRC> updateRC(
            @PathVariable Long vehicleId,
            @RequestBody VehicleRC updated
    ) {
        VehicleRC rc =
                rcRepository.findByVehicleId(vehicleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "RC not found"
                                )
                        );

        if (updated.getRcNumber() != null) {
            rc.setRcNumber(
                    updated.getRcNumber()
            );
        }

        if (updated.getRegistrationDate() != null) {
            rc.setRegistrationDate(
                    updated.getRegistrationDate()
            );
        }

        if (updated.getRegistrationExpiry() != null) {
            rc.setRegistrationExpiry(
                    updated.getRegistrationExpiry()
            );
        }

        if (updated.getOwnerName() != null) {
            rc.setOwnerName(
                    updated.getOwnerName()
            );
        }

        if (updated.getStatus() != null) {
            rc.setStatus(
                    updated.getStatus()
            );
        }

        return ResponseEntity.ok(
                rcRepository.save(rc)
        );
    }

    // =========================================================
    // FITNESS
    // =========================================================

    @GetMapping("/{vehicleId}/fitness")
    public ResponseEntity<VehicleFitness> getFitness(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return fitnessRepository
                .findByVehicleId(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PostMapping("/{vehicleId}/fitness")
    public ResponseEntity<VehicleFitness> addFitness(
            @PathVariable Long vehicleId,
            @RequestBody VehicleFitness fitness
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        if (fitnessRepository.existsByVehicleId(vehicleId)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        fitness.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fitnessRepository.save(fitness));
    }

    @PutMapping("/{vehicleId}/fitness")
    public ResponseEntity<VehicleFitness> updateFitness(
            @PathVariable Long vehicleId,
            @RequestBody VehicleFitness updated
    ) {
        VehicleFitness fitness =
                fitnessRepository.findByVehicleId(vehicleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Fitness record not found"
                                )
                        );

        if (updated.getCertificateNumber() != null) {
            fitness.setCertificateNumber(
                    updated.getCertificateNumber()
            );
        }

        if (updated.getIssueDate() != null) {
            fitness.setIssueDate(
                    updated.getIssueDate()
            );
        }

        if (updated.getExpiryDate() != null) {
            fitness.setExpiryDate(
                    updated.getExpiryDate()
            );
        }

        if (updated.getStatus() != null) {
            fitness.setStatus(
                    updated.getStatus()
            );
        }

        return ResponseEntity.ok(
                fitnessRepository.save(fitness)
        );
    }

    // =========================================================
    // SERVICE / MAINTENANCE
    // =========================================================

    @GetMapping("/{vehicleId}/services")
    public ResponseEntity<List<com.security360.security360_backend.entity.VehicleService>> getServices(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return ResponseEntity.ok(
                serviceRepository
                        .findByVehicleIdOrderByServiceDateDesc(vehicleId)
        );
    }

    @PostMapping("/{vehicleId}/services")
    public ResponseEntity<com.security360.security360_backend.entity.VehicleService> addService(
            @PathVariable Long vehicleId,
            @RequestBody com.security360.security360_backend.entity.VehicleService service
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        service.setVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceRepository.save(service));
    }

    @PutMapping("/{vehicleId}/services/{serviceId}")
    public ResponseEntity<com.security360.security360_backend.entity.VehicleService> updateService(
            @PathVariable Long vehicleId,
            @PathVariable Long serviceId,
            @RequestBody com.security360.security360_backend.entity.VehicleService updated
    ) {
        com.security360.security360_backend.entity.VehicleService service =
                serviceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service record not found"
                                )
                        );

        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        service.setVehicle(vehicle);

        if (updated.getServiceDate() != null) {
            service.setServiceDate(
                    updated.getServiceDate()
            );
        }

        if (updated.getServiceType() != null) {
            service.setServiceType(
                    updated.getServiceType()
            );
        }

        if (updated.getDescription() != null) {
            service.setDescription(
                    updated.getDescription()
            );
        }

        if (updated.getOdometer() != null) {
            service.setOdometer(
                    updated.getOdometer()
            );
        }

        if (updated.getCost() != null) {
            service.setCost(
                    updated.getCost()
            );
        }

        if (updated.getNextServiceDate() != null) {
            service.setNextServiceDate(
                    updated.getNextServiceDate()
            );
        }

        if (updated.getServiceCenter() != null) {
            service.setServiceCenter(
                    updated.getServiceCenter()
            );
        }

        return ResponseEntity.ok(
                serviceRepository.save(service)
        );
    }

    @DeleteMapping("/{vehicleId}/services/{serviceId}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long vehicleId,
            @PathVariable Long serviceId
    ) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException(
                    "Service record not found"
            );
        }

        serviceRepository.deleteById(serviceId);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // TRIP HISTORY
    // =========================================================

    @GetMapping("/{vehicleId}/trips")
    public ResponseEntity<List<Trip>> getTrips(
            @PathVariable Long vehicleId
    ) {
        vehicleService.getVehicleById(vehicleId);

        return ResponseEntity.ok(
                tripRepository
                        .findByVehicleIdOrderByStartTimeDesc(vehicleId)
        );
    }

    @PostMapping("/{vehicleId}/trips")
    public ResponseEntity<Trip> addTrip(
            @PathVariable Long vehicleId,
            @RequestBody Trip trip
    ) {
        Vehicle vehicle =
                vehicleService.getVehicleById(vehicleId);

        trip.setVehicle(vehicle);

        if (trip.getDriver() != null &&
                trip.getDriver().getId() != null) {

            Driver driver =
                    driverRepository.findById(
                            trip.getDriver().getId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Driver not found"
                            )
                    );

            trip.setDriver(driver);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripRepository.save(trip));
    }

    @DeleteMapping("/{vehicleId}/trips/{tripId}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long vehicleId,
            @PathVariable Long tripId
    ) {
        if (!tripRepository.existsById(tripId)) {
            throw new RuntimeException(
                    "Trip not found"
            );
        }

        tripRepository.deleteById(tripId);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // DASHBOARD REPORT
    // =========================================================

    @GetMapping("/reports/summary")
    public ResponseEntity<?> getVehicleSummary() {

        List<Vehicle> vehicles =
                vehicleService.getAllVehicles();

        long total = vehicles.size();

        long active = vehicles.stream()
                .filter(v ->
                        "ACTIVE".equalsIgnoreCase(
                                v.getStatus()
                        ))
                .count();

        long inactive = vehicles.stream()
                .filter(v ->
                        "INACTIVE".equalsIgnoreCase(
                                v.getStatus()
                        ))
                .count();

        long gpsEnabled = vehicles.stream()
                .filter(v ->
                        Boolean.TRUE.equals(
                                v.getGpsEnabled()
                        ))
                .count();

        return ResponseEntity.ok(
                java.util.Map.of(
                        "totalVehicles", total,
                        "activeVehicles", active,
                        "inactiveVehicles", inactive,
                        "gpsEnabledVehicles", gpsEnabled
                )
        );
    }
}