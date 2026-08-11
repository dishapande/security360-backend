package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.GuardLocationDTO;
import com.security360.security360_backend.dto.GuardTrackingResponseDTO;
import com.security360.security360_backend.entity.Employee;
import com.security360.security360_backend.entity.GuardLocation;
import com.security360.security360_backend.entity.GuardLocationHistory;
import com.security360.security360_backend.repository.EmployeeRepository;
import com.security360.security360_backend.repository.GuardLocationHistoryRepository;
import com.security360.security360_backend.repository.GuardLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "http://localhost:8081")
public class GuardTrackingController {

    @Autowired
    private GuardLocationRepository locationRepository;

    @Autowired
    private GuardLocationHistoryRepository historyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. POST: Update a Guard's Live Location (Called by the Mobile App every 30 seconds)
    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(@RequestBody GuardLocationDTO request) {
        try {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // Save to History
            GuardLocationHistory history = new GuardLocationHistory();
            history.setEmployee(employee);
            history.setLatitude(request.getLatitude());
            history.setLongitude(request.getLongitude());
            history.setTimestamp(LocalDateTime.now());
            historyRepository.save(history);

            // Upsert (Update or Insert) into Live Location
            GuardLocation live = locationRepository.findByEmployeeId(request.getEmployeeId());
            if (live == null) {
                live = new GuardLocation();
                live.setEmployee(employee);
            }
            live.setLatitude(request.getLatitude());
            live.setLongitude(request.getLongitude());
            live.setSpeedKmh(request.getSpeedKmh());
            live.setBatteryPercent(request.getBatteryPercent());
            live.setNetworkType(request.getNetworkType());
            live.setCheckpoint(request.getCheckpoint());
            live.setLastSeen(LocalDateTime.now());

            locationRepository.save(live);
            return ResponseEntity.ok("Location updated for Guard: " + employee.getFullName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating location: " + e.getMessage());
        }
    }

    // 2. GET: Fetch All Live Guard Locations (For Frontend Map)
    @GetMapping("/live")
    public ResponseEntity<List<GuardTrackingResponseDTO>> getLiveGuards() {
        List<GuardLocation> liveLocations = locationRepository.findAll();
        List<GuardTrackingResponseDTO> response = new ArrayList<>();

        for (GuardLocation loc : liveLocations) {
            GuardTrackingResponseDTO dto = new GuardTrackingResponseDTO();
            dto.setEmployeeId(loc.getEmployee().getId());
            dto.setFullName(loc.getEmployee().getFullName());
            dto.setEmpCode(loc.getEmployee().getEmpCode());
            dto.setRole(loc.getEmployee().getRole());
            dto.setStatus("Active"); // You can map this based on lastSeen time
            dto.setLatitude(loc.getLatitude());
            dto.setLongitude(loc.getLongitude());
            dto.setSpeedKmh(loc.getSpeedKmh());
            dto.setBatteryPercent(loc.getBatteryPercent());
            dto.setNetworkType(loc.getNetworkType());
            dto.setCheckpoint(loc.getCheckpoint());
            dto.setLastSeen(loc.getLastSeen());
            response.add(dto);
        }
        return ResponseEntity.ok(response);
    }

    // 3. GET: Fetch History for Route Playback (Last 24 hours)
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<List<GuardLocationHistory>> getGuardHistory(@PathVariable Long employeeId) {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<GuardLocationHistory> history = historyRepository.findByEmployeeIdAndTimestampAfter(employeeId, twentyFourHoursAgo);
        return ResponseEntity.ok(history);
    }
}