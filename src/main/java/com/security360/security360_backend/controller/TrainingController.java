package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.TrainingCourse;
import com.security360.security360_backend.entity.TrainingSession;
import com.security360.security360_backend.service.TrainingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = {
        "http://localhost:8081"
})
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    // =========================
    // COURSE APIs
    // =========================

    @GetMapping("/courses")
    public ResponseEntity<List<TrainingCourse>> getCourses(
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                trainingService.getCourses(search)
        );
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<TrainingCourse> getCourse(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trainingService.getCourse(id)
        );
    }

    @PostMapping("/courses")
    public ResponseEntity<TrainingCourse> createCourse(
            @RequestBody TrainingCourse course) {

        TrainingCourse savedCourse =
                trainingService.createCourse(course);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCourse);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<TrainingCourse> updateCourse(
            @PathVariable Long id,
            @RequestBody TrainingCourse course) {

        return ResponseEntity.ok(
                trainingService.updateCourse(id, course)
        );
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id) {

        trainingService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // SESSION APIs
    // =========================

    @GetMapping("/sessions")
    public ResponseEntity<List<TrainingSession>> getSessions(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        // ✅ Date-range filtering disabled until TrainingSession.date becomes LocalDate
        //    (currently a String, so getSessions(LocalDate, LocalDate) is not available)
        return ResponseEntity.ok(
                trainingService.getSessions()
        );
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<TrainingSession> getSession(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trainingService.getSession(id)
        );
    }

    @PostMapping("/sessions")
    public ResponseEntity<TrainingSession> createSession(
            @RequestParam Long courseId,
            @RequestBody TrainingSession session) {

        TrainingSession savedSession =
                trainingService.createSession(
                        session,
                        courseId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedSession);
    }

    @PutMapping("/sessions/{id}")
    public ResponseEntity<TrainingSession> updateSession(
            @PathVariable Long id,
            @RequestParam(required = false) Long courseId,
            @RequestBody TrainingSession session) {

        return ResponseEntity.ok(
                trainingService.updateSession(
                        id,
                        session,
                        courseId
                )
        );
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long id) {

        trainingService.deleteSession(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // DASHBOARD
    // =========================

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {

        List<TrainingCourse> courses =
                trainingService.getCourses(null);

        List<TrainingSession> sessions =
                trainingService.getSessions();

        Map<String, Object> result =
                new HashMap<>();

        result.put("programs", courses.size());
        result.put("sessions", sessions.size());
        result.put("enrolled", 0);
        result.put("completed", 0);
        result.put("ongoing", 0);
        result.put("averageScore", 0);
        result.put("certified", 0);

        return ResponseEntity.ok(result);
    }

    // =========================
    // ERROR HANDLING
    // =========================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(
            IllegalArgumentException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                exception.getMessage() != null
                        ? exception.getMessage()
                        : "Invalid request"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                exception.getMessage() != null
                        ? exception.getMessage()
                        : "Something went wrong"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}