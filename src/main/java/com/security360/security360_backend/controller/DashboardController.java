package com.security360.security360_backend.controller;

import com.security360.security360_backend.dto.LiveDashboardStats;
import com.security360.security360_backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:8081") 
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // This endpoint will push Live Data to React every 5 seconds
    @GetMapping("/stream")
    public SseEmitter streamLiveData() {
        // Set the timeout to 1 hour. If the connection drops, React will auto-reconnect.
        SseEmitter emitter = new SseEmitter(3_600_000L);
        
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        
        // Schedule a task to run every 5 seconds
        executor.scheduleAtFixedRate(() -> {
            try {
                // 1. Fetch latest data from Service
                LiveDashboardStats latestStats = dashboardService.getLatestLiveStats();
                
                // 2. Send the data to React
                emitter.send(SseEmitter.event()
                        .name("dashboard-update") // Event name React will listen to
                        .data(latestStats));
                
            } catch (IOException e) {
                // If the connection closes, cancel the task and complete the emitter
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 5, TimeUnit.SECONDS); // Send first event immediately, then every 5 seconds

        // Clean up the executor when the emitter completes (user closes browser)
        emitter.onCompletion(() -> executor.shutdown());
        emitter.onTimeout(() -> executor.shutdown());

        return emitter;
    }
}