package com.security360.security360_backend.dto;

import java.util.List;
import java.util.Map;

public class LiveDashboardStats {
    // Hero Widgets
    private int guardsOnline;
    private int activePatrols;
    private int sosAlerts;
    
    // Weather Widget
    private Map<String, Object> weather;
    
    // Notifications Widget
    private List<Map<String, String>> notifications;

    // Constructor, Getters, and Setters
    public LiveDashboardStats() {}

    public LiveDashboardStats(int guardsOnline, int activePatrols, int sosAlerts, Map<String, Object> weather, List<Map<String, String>> notifications) {
        this.guardsOnline = guardsOnline;
        this.activePatrols = activePatrols;
        this.sosAlerts = sosAlerts;
        this.weather = weather;
        this.notifications = notifications;
    }

    // Getters & Setters (Required for Jackson JSON serialization)
    public int getGuardsOnline() { return guardsOnline; }
    public void setGuardsOnline(int guardsOnline) { this.guardsOnline = guardsOnline; }
    public int getActivePatrols() { return activePatrols; }
    public void setActivePatrols(int activePatrols) { this.activePatrols = activePatrols; }
    public int getSosAlerts() { return sosAlerts; }
    public void setSosAlerts(int sosAlerts) { this.sosAlerts = sosAlerts; }
    public Map<String, Object> getWeather() { return weather; }
    public void setWeather(Map<String, Object> weather) { this.weather = weather; }
    public List<Map<String, String>> getNotifications() { return notifications; }
    public void setNotifications(List<Map<String, String>> notifications) { this.notifications = notifications; }
}