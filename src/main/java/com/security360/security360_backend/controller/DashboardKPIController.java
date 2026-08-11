package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.DashboardKPIDTO;
import com.security360.security360_backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardKPIController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/kpis")
    public DashboardKPIDTO getDashboardKPIs() {
        DashboardKPIDTO kpi = new DashboardKPIDTO();

        // 1. REAL DATA FROM MYSQL
        long totalEmployees = employeeRepository.count(); // Counts the rows in your employee table!
        long activeEmployees = employeeRepository.countByStatus("Active");
        long onLeaveEmployees = employeeRepository.countByStatus("On Leave");
        long inactiveEmployees = employeeRepository.countByStatus("Inactive");

        // 2. MAP TO DTO
        kpi.setTotalGuards(totalEmployees);
        kpi.setActiveGuards(activeEmployees);
        kpi.setAbsentGuards(onLeaveEmployees); // Treating "On Leave" as Absent for now
        kpi.setLateCheckins(0); // Replace later with real data
        
        // 3. OTHER MOCK DATA (Until you build Clients, Incidents, etc.)
        kpi.setSitesCovered(612);
        kpi.setActiveClients(184);
        kpi.setOpenIncidents(27);
        kpi.setEmergencyAlerts(3);
        kpi.setVisitorsToday(1942);
        kpi.setVehiclesToday(738);
        kpi.setMonthlyRevenue(4.62);

        return kpi;
    }
}