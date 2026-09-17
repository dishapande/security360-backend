package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.AttendanceRecord;
import com.security360.security360_backend.entity.Employee;
import com.security360.security360_backend.repository.AttendanceRepository;
import com.security360.security360_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salary")
@CrossOrigin(origins = "http://localhost:8081")
public class SalaryController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private AttendanceRepository attendanceRepository;

    // 1. GET: Calculate salary for ONE employee for a specific month
    @GetMapping("/calculate/{employeeId}")
    public ResponseEntity<Map<String, Object>> calculateSalary(
            @PathVariable Long employeeId,
            @RequestParam int year,
            @RequestParam int month) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<AttendanceRecord> records = attendanceRepository
                .findByEmployeeIdAndPunchInTimeBetween(employeeId, start, end);

        long presentDays = records.stream()
                .filter(r -> "Active".equalsIgnoreCase(r.getStatus()) || "Present".equalsIgnoreCase(r.getStatus()))
                .count();

        long lateDays = records.stream()
                .filter(r -> "Late".equalsIgnoreCase(r.getStatus()))
                .count();

        long absentDays = records.stream()
                .filter(r -> "Absent".equalsIgnoreCase(r.getStatus()))
                .count();

        Double dailyWage = employee.getDailyWage() != null ? employee.getDailyWage() : 500.0;
        Double basicSalary = employee.getBasicSalary() != null ? employee.getBasicSalary() : 15000.0;

        double earnedSalary = presentDays * dailyWage;
        double lateDeduction = lateDays * (dailyWage * 0.25); // 25% deduction for late
        double absentDeduction = absentDays * dailyWage; // 100% deduction for absent
        double netSalary = earnedSalary - lateDeduction - absentDeduction;

        if (netSalary < 0) netSalary = 0;

        Map<String, Object> response = new HashMap<>();
        response.put("employeeName", employee.getFullName());
        response.put("employeeId", employee.getId());
        response.put("year", year);
        response.put("month", month);
        response.put("presentDays", presentDays);
        response.put("lateDays", lateDays);
        response.put("absentDays", absentDays);
        response.put("basicSalary", basicSalary);
        response.put("dailyWage", dailyWage);
        response.put("earnedSalary", earnedSalary);
        response.put("lateDeduction", lateDeduction);
        response.put("absentDeduction", absentDeduction);
        response.put("netSalary", netSalary);

        return ResponseEntity.ok(response);
    }

    // 2. GET: Calculate salary for ALL employees for a specific month
    @GetMapping("/calculate-all")
    public ResponseEntity<List<Map<String, Object>>> calculateAllSalaries(
            @RequestParam int year,
            @RequestParam int month) {

        List<Employee> employees = employeeRepository.findAll();
        List<Map<String, Object>> allSalaries = new ArrayList<>();

        for (Employee employee : employees) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);

            List<AttendanceRecord> records = attendanceRepository
                    .findByEmployeeIdAndPunchInTimeBetween(employee.getId(), start, end);

            long presentDays = records.stream()
                    .filter(r -> "Active".equalsIgnoreCase(r.getStatus()) || "Present".equalsIgnoreCase(r.getStatus()))
                    .count();

            long lateDays = records.stream()
                    .filter(r -> "Late".equalsIgnoreCase(r.getStatus()))
                    .count();

            long absentDays = records.stream()
                    .filter(r -> "Absent".equalsIgnoreCase(r.getStatus()))
                    .count();

            Double dailyWage = employee.getDailyWage() != null ? employee.getDailyWage() : 500.0;
            Double basicSalary = employee.getBasicSalary() != null ? employee.getBasicSalary() : 15000.0;

            double earnedSalary = presentDays * dailyWage;
            double lateDeduction = lateDays * (dailyWage * 0.25);
            double absentDeduction = absentDays * dailyWage;
            double netSalary = earnedSalary - lateDeduction - absentDeduction;

            if (netSalary < 0) netSalary = 0;

            Map<String, Object> salaryData = new HashMap<>();
            salaryData.put("employeeName", employee.getFullName());
            salaryData.put("employeeId", employee.getId());
            salaryData.put("presentDays", presentDays);
            salaryData.put("lateDays", lateDays);
            salaryData.put("absentDays", absentDays);
            salaryData.put("dailyWage", dailyWage);
            salaryData.put("basicSalary", basicSalary);
            salaryData.put("earnedSalary", earnedSalary);
            salaryData.put("lateDeduction", lateDeduction);
            salaryData.put("absentDeduction", absentDeduction);
            salaryData.put("netSalary", netSalary);

            allSalaries.add(salaryData);
        }

        return ResponseEntity.ok(allSalaries);
    }
}