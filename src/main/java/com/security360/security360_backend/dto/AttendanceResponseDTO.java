package com.security360.security360_backend.dto;

import java.time.LocalDateTime;

public class AttendanceResponseDTO {
    private Long id;
    private String employeeId; // emp_code
    private String employeeName;
    private String role;
    private String site;
    private String punchMethod;
    private String status;
    private String shiftType;
    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getPunchMethod() { return punchMethod; }
    public void setPunchMethod(String punchMethod) { this.punchMethod = punchMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }

    public LocalDateTime getPunchInTime() { return punchInTime; }
    public void setPunchInTime(LocalDateTime punchInTime) { this.punchInTime = punchInTime; }

    public LocalDateTime getPunchOutTime() { return punchOutTime; }
    public void setPunchOutTime(LocalDateTime punchOutTime) { this.punchOutTime = punchOutTime; }
}