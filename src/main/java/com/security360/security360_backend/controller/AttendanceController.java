package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.AttendancePunchDTO;
import com.security360.security360_backend.dto.AttendanceResponseDTO;
import com.security360.security360_backend.entity.AttendanceRecord;
import com.security360.security360_backend.entity.Employee;
import com.security360.security360_backend.repository.AttendanceRepository;
import com.security360.security360_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:8081")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // --- CONFIGURATION CONSTANTS ---
    private static final double GEO_FENCE_RADIUS_METERS = 500.0; // 500 meters radius allowed
    private static final String VALID_QR_SECRET = "SECURE_QR_2026"; // Mocked secret for QR validation

    // 1. GET: Fetch Today's Attendance Table
    @GetMapping("/today")
    public ResponseEntity<List<AttendanceResponseDTO>> getTodayAttendance() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<AttendanceRecord> records = attendanceRepository.findByPunchInTimeBetween(startOfDay, endOfDay);
        List<AttendanceResponseDTO> response = new ArrayList<>();

        for (AttendanceRecord record : records) {
            response.add(mapToDTO(record));
        }
        return ResponseEntity.ok(response);
    }

    // 2. POST: Punch In (With Geo, Face, and QR Logic)
    @PostMapping("/punch-in")
    public ResponseEntity<String> punchIn(@RequestBody AttendancePunchDTO request) {
        try {
            // 1. Find the Employee
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            LocalDateTime now = LocalDateTime.now();
            LocalTime shiftStart = LocalTime.of(9, 0);
            String status = now.toLocalTime().isAfter(shiftStart) ? "Late" : "Active";

            // 2. ----- VALIDATE PUNCH METHOD -----
            String punchMethod = request.getPunchMethod();
            
            // 2a. FACE / BIO VALIDATION
            if ("Face".equalsIgnoreCase(punchMethod)) {
                // In a real app, you would send a Base64 image to Azure/Amazon Rekognition here.
                // For now, we assume it's valid if the employee exists.
                // If the image fails verification, you would throw a RuntimeException here.
            }

            // 2b. QR CODE VALIDATION
            if ("QR".equalsIgnoreCase(punchMethod)) {
                // Validate if the token provided in the DTO matches
                if (request.getQrToken() == null || !request.getQrToken().equals(VALID_QR_SECRET)) {
                    throw new RuntimeException("Invalid QR Code. Access Denied.");
                }
            }

            // 2c. GEO FENCE VALIDATION
            if ("Geo Fence".equalsIgnoreCase(punchMethod)) {
                // Ensure coordinates were sent
                if (request.getGeoLatitude() == null || request.getGeoLongitude() == null) {
                    throw new RuntimeException("GPS Coordinates required for Geo Fence Punch.");
                }

                // -------------------------------------------------------------------------
                // ⚠️ MOCK DATA: Replace these coords with actual site coordinates from your DB
                // -------------------------------------------------------------------------
                double siteLatitude = 28.6139; // Example: Delhi HQ Latitude
                double siteLongitude = 77.2090; // Example: Delhi HQ Longitude

                // Calculate distance using Haversine formula
                double distance = calculateDistance(
                        request.getGeoLatitude(), request.getGeoLongitude(),
                        siteLatitude, siteLongitude
                );

                // If the guard is too far away, mark them as "Offsite" or reject the punch
                if (distance > GEO_FENCE_RADIUS_METERS) {
                    return ResponseEntity.badRequest().body("Geo Fence Alert: You are " + Math.round(distance) + " meters away from the assigned site. Access Denied.");
                }
            }

            // 3. Save the record if all validations pass
            AttendanceRecord record = new AttendanceRecord(
                employee, now, punchMethod, status
            );
            record.setShiftType(now.toLocalTime().isBefore(LocalTime.NOON) ? "Day" : "Night");
            record.setGeoLatitude(request.getGeoLatitude());
            record.setGeoLongitude(request.getGeoLongitude());

            attendanceRepository.save(record);
            return ResponseEntity.ok("Punched In successfully via " + punchMethod + " at " + now);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error punching in: " + e.getMessage());
        }
    }

    // 3. POST: Punch Out
    @PostMapping("/punch-out")
    public ResponseEntity<String> punchOut(@RequestBody AttendancePunchDTO request) {
        try {
            List<AttendanceRecord> records = attendanceRepository.findByEmployeeId(request.getEmployeeId());
            
            AttendanceRecord openRecord = records.stream()
                    .sorted(Comparator.comparing(AttendanceRecord::getPunchInTime).reversed())
                    .filter(r -> r.getPunchOutTime() == null)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No active shift found to punch out from"));

            openRecord.setPunchOutTime(LocalDateTime.now());
            attendanceRepository.save(openRecord);
            return ResponseEntity.ok("Punched Out successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error punching out: " + e.getMessage());
        }
    }

    // 4. PUT: Update Status (For Mark Late / Mark Absent Actions)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        try {
            AttendanceRecord record = attendanceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Attendance record not found"));
            record.setStatus(newStatus);
            attendanceRepository.save(record);
            return ResponseEntity.ok("Status updated to " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }

    // 5. GET: Generate Report
    @GetMapping("/report")
    public ResponseEntity<String> getReport() {
        return ResponseEntity.ok("{\"message\":\"Report generated successfully. Total present today: " + attendanceRepository.findByPunchInTimeBetween(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX)).size() + "\"}");
    }

    // --- HELPER METHODS ---

    private AttendanceResponseDTO mapToDTO(AttendanceRecord record) {
        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        dto.setId(record.getId());
        dto.setEmployeeId(record.getEmployee().getEmpCode());
        dto.setEmployeeName(record.getEmployee().getFullName());
        dto.setRole(record.getEmployee().getRole());
        dto.setSite(record.getEmployee().getBranch());
        dto.setPunchMethod(record.getPunchMethod());
        dto.setStatus(record.getStatus());
        dto.setShiftType(record.getShiftType());
        dto.setPunchInTime(record.getPunchInTime());
        dto.setPunchOutTime(record.getPunchOutTime());
        return dto;
    }

    // Haversine formula to calculate distance between two GPS coordinates in meters
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
    }
    // 6. POST: Upload Face Image
    @PostMapping("/upload-face")
    public ResponseEntity<String> uploadFaceImage(@RequestBody Map<String, String> payload) {
        try {
            String base64Image = payload.get("image");
            String employeeId = payload.get("employeeId");

            // Remove the "data:image/jpeg;base64," header
            String[] parts = base64Image.split(",");
            String imageString = parts[1];

            // Decode Base64 to bytes using Java 11+ Base64 decoder
            byte[] imageBytes = java.util.Base64.getDecoder().decode(imageString);

            // Define where to save the file (Create a folder named 'faces' in your project root)
            String folderPath = System.getProperty("user.dir") + "/faces/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Save the file
            String fileName = "face_" + employeeId + "_" + System.currentTimeMillis() + ".jpg";
            FileOutputStream fos = new FileOutputStream(folderPath + fileName);
            fos.write(imageBytes);
            fos.close();

            return ResponseEntity.ok("Face image saved successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving face: " + e.getMessage());
        }
    }
}