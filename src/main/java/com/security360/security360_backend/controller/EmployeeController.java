package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.EmployeeDTO;
import com.security360.security360_backend.entity.Employee;
import com.security360.security360_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. GET: Fetch all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    // 2. POST: Add a new employee (UPDATED to save Code and Branch)
    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee newEmployee = new Employee(
                employeeDTO.getFullName(),
                employeeDTO.getEmail(),
                employeeDTO.getRole()
            );
            
            // 🟢 FIX: Explicitly save the Code and Branch here!
            newEmployee.setEmpCode(employeeDTO.getEmpCode());
            newEmployee.setBranch(employeeDTO.getBranch());
            newEmployee.setPhone(employeeDTO.getPhone());
            newEmployee.setStatus(employeeDTO.getStatus());

            employeeRepository.save(newEmployee);
            return ResponseEntity.ok("Employee added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding employee: " + e.getMessage());
        }
    }

    // 3. PUT: Update an existing employee by ID (UPDATED to save Code and Branch)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee existingEmployee = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

            existingEmployee.setFullName(employeeDTO.getFullName());
            existingEmployee.setEmail(employeeDTO.getEmail());
            existingEmployee.setRole(employeeDTO.getRole());
            
            // 🟢 FIX: Explicitly update the Code and Branch here!
            existingEmployee.setEmpCode(employeeDTO.getEmpCode());
            existingEmployee.setBranch(employeeDTO.getBranch());
            existingEmployee.setPhone(employeeDTO.getPhone());
            existingEmployee.setStatus(employeeDTO.getStatus());

            employeeRepository.save(existingEmployee);
            return ResponseEntity.ok("Employee updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating employee: " + e.getMessage());
        }
    }

    // 4. DELETE: Delete an employee by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            if (!employeeRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Employee not found with ID: " + id);
            }
            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Employee deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting employee: " + e.getMessage());
        }
    }
}