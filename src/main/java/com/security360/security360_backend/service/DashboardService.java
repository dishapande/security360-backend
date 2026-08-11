package com.security360.security360_backend.service;

import com.security360.security360_backend.dto.LiveDashboardStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DashboardService {

    public LiveDashboardStats getLatestLiveStats() {
        return generateMockStats();
    }

    // Logic to generate Mock Live Data (Replace this with real Database/API calls later)
    private LiveDashboardStats generateMockStats() {
        // 1. Simulate Widgets
        int guardsOnline = ThreadLocalRandom.current().nextInt(3800, 4000);
        int activePatrols = ThreadLocalRandom.current().nextInt(270, 300);
        int sosAlerts = ThreadLocalRandom.current().nextInt(0, 3);

        // 2. Simulate Weather
        Map<String, Object> weather = new HashMap<>();
        weather.put("temp", 28);
        weather.put("condition", "Partly cloudy");
        weather.put("icon", "☀️");

        // 3. Simulate Notifications
        List<Map<String, String>> notifications = new ArrayList<>();
        Map<String, String> notif1 = new HashMap<>();
        notif1.put("id", UUID.randomUUID().toString());
        notif1.put("msg", "New guard onboarded in Mumbai");
        notif1.put("time", "Just now");
        
        Map<String, String> notif2 = new HashMap<>();
        notif2.put("id", UUID.randomUUID().toString());
        notif2.put("msg", "Patrol #482 completed at T3");
        notif2.put("time", "2m ago");
        
        notifications.add(notif1);
        notifications.add(notif2);

        return new LiveDashboardStats(guardsOnline, activePatrols, sosAlerts, weather, notifications);
    }
}