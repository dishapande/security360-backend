package com.security360.security360_backend.dto;

public class DashboardKPIDTO {
    private long totalGuards;
    private long activeGuards;
    private long absentGuards;
    private long lateCheckins;
    private long sitesCovered;
    private long activeClients;
    private long openIncidents;
    private long emergencyAlerts;
    private long visitorsToday;
    private long vehiclesToday;
    private double monthlyRevenue;

    // Getters and Setters (Required for Jackson JSON serialization)
    public long getTotalGuards() { return totalGuards; }
    public void setTotalGuards(long totalGuards) { this.totalGuards = totalGuards; }
    
    public long getActiveGuards() { return activeGuards; }
    public void setActiveGuards(long activeGuards) { this.activeGuards = activeGuards; }
    
    public long getAbsentGuards() { return absentGuards; }
    public void setAbsentGuards(long absentGuards) { this.absentGuards = absentGuards; }
    
    public long getLateCheckins() { return lateCheckins; }
    public void setLateCheckins(long lateCheckins) { this.lateCheckins = lateCheckins; }
    
    public long getSitesCovered() { return sitesCovered; }
    public void setSitesCovered(long sitesCovered) { this.sitesCovered = sitesCovered; }
    
    public long getActiveClients() { return activeClients; }
    public void setActiveClients(long activeClients) { this.activeClients = activeClients; }
    
    public long getOpenIncidents() { return openIncidents; }
    public void setOpenIncidents(long openIncidents) { this.openIncidents = openIncidents; }
    
    public long getEmergencyAlerts() { return emergencyAlerts; }
    public void setEmergencyAlerts(long emergencyAlerts) { this.emergencyAlerts = emergencyAlerts; }
    
    public long getVisitorsToday() { return visitorsToday; }
    public void setVisitorsToday(long visitorsToday) { this.visitorsToday = visitorsToday; }
    
    public long getVehiclesToday() { return vehiclesToday; }
    public void setVehiclesToday(long vehiclesToday) { this.vehiclesToday = vehiclesToday; }
    
    public double getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(double monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
}