package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.TrainingCourse;
import com.security360.security360_backend.entity.TrainingSession;
import com.security360.security360_backend.repository.TrainingCourseRepository;
import com.security360.security360_backend.repository.TrainingSessionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingService {

    private final TrainingCourseRepository courseRepository;
    private final TrainingSessionRepository sessionRepository;

    public TrainingService(
            TrainingCourseRepository courseRepository,
            TrainingSessionRepository sessionRepository) {

        this.courseRepository = courseRepository;
        this.sessionRepository = sessionRepository;
    }

    // =========================================================
    // COURSES
    // =========================================================

    public List<TrainingCourse> getCourses(String search) {
        if (search == null || search.trim().isEmpty()) {
            return courseRepository.findAll();
        }
        String value = search.trim();
        return courseRepository
                .findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        value, value, value);
    }

    public TrainingCourse getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Transactional
    public TrainingCourse createCourse(TrainingCourse course) {
        if (course == null) throw new IllegalArgumentException("Course data is required");
        if (course.getName() == null || course.getName().trim().isEmpty())
            throw new IllegalArgumentException("Course name is required");
        if (course.getHrs() == null || course.getHrs() <= 0)
            throw new IllegalArgumentException("Training hours must be greater than 0");
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty())
            throw new IllegalArgumentException("Instructor is required");

        course.setName(course.getName().trim());
        course.setInstructor(course.getInstructor().trim());

        if (course.getCategory() == null || course.getCategory().trim().isEmpty())
            course.setCategory("General");
        if (course.getStatus() == null || course.getStatus().trim().isEmpty())
            course.setStatus("Pending");
        if (course.getEnrolled() == null) course.setEnrolled(0);
        if (course.getCompletion() == null) course.setCompletion(0);

        // ✅ Use courseCode, not code
        if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            course.setCourseCode("TRN-" + (courseRepository.count() + 101));
        }

        return courseRepository.save(course);
    }

    @Transactional
    public TrainingCourse updateCourse(Long id, TrainingCourse input) {
        if (input == null) throw new IllegalArgumentException("Course data is required");

        TrainingCourse course = getCourse(id);

        if (input.getName() != null && !input.getName().trim().isEmpty())
            course.setName(input.getName().trim());

        // ✅ Use hrs, not hours
        if (input.getHrs() != null) {
            if (input.getHrs() <= 0)
                throw new IllegalArgumentException("Training hours must be greater than 0");
            course.setHrs(input.getHrs());
        }

        if (input.getInstructor() != null && !input.getInstructor().trim().isEmpty())
            course.setInstructor(input.getInstructor().trim());

        if (input.getCategory() != null) course.setCategory(input.getCategory().trim());
        if (input.getStatus() != null) course.setStatus(input.getStatus().trim());
        if (input.getEnrolled() != null) course.setEnrolled(input.getEnrolled());
        if (input.getCompletion() != null) course.setCompletion(input.getCompletion());

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        TrainingCourse course = getCourse(id);
        // ✅ Sessions no longer hold a TrainingCourse reference — just delete the course
        courseRepository.delete(course);
    }

    // =========================================================
    // SESSIONS
    // =========================================================

    public List<TrainingSession> getSessions() {
        return sessionRepository.findAll();
    }

    public TrainingSession getSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training session not found with id: " + id));
    }

    @Transactional
    public TrainingSession createSession(TrainingSession session, Long courseId) {
        if (session == null) throw new IllegalArgumentException("Training session data is required");
        if (courseId == null) throw new IllegalArgumentException("Course ID is required");

        TrainingCourse course = getCourse(courseId);

        if (session.getDate() == null) throw new IllegalArgumentException("Training date is required");
        if (session.getTime() == null) throw new IllegalArgumentException("Training time is required");
        if (session.getTrainer() == null || session.getTrainer().trim().isEmpty())
            throw new IllegalArgumentException("Trainer is required");
        if (session.getLocation() == null || session.getLocation().trim().isEmpty())
            throw new IllegalArgumentException("Location is required");

        // ✅ Entity uses courseCode / courseName (not a nested object)
        session.setCourseCode(course.getCourseCode());
        session.setCourseName(course.getName());

        session.setTrainer(session.getTrainer().trim());
        session.setLocation(session.getLocation().trim());

        if (session.getCapacity() == null) session.setCapacity(0);
        if (session.getStatus() == null || session.getStatus().trim().isEmpty())
            session.setStatus("Upcoming");

        // ✅ Use sessionCode, not trainingCode
        if (session.getSessionCode() == null || session.getSessionCode().trim().isEmpty()) {
            session.setSessionCode(
                    "TRAIN-" + String.format("%03d", sessionRepository.count() + 1)
            );
        }

        return sessionRepository.save(session);
    }

    @Transactional
    public TrainingSession updateSession(Long id, TrainingSession input, Long courseId) {
        if (input == null) throw new IllegalArgumentException("Training session data is required");

        TrainingSession session = getSession(id);

        if (courseId != null) {
            TrainingCourse course = getCourse(courseId);
            session.setCourseCode(course.getCourseCode());
            session.setCourseName(course.getName());
        }

        if (input.getDate() != null) session.setDate(input.getDate());
        if (input.getTime() != null) session.setTime(input.getTime());

        if (input.getTrainer() != null && !input.getTrainer().trim().isEmpty())
            session.setTrainer(input.getTrainer().trim());

        if (input.getLocation() != null && !input.getLocation().trim().isEmpty())
            session.setLocation(input.getLocation().trim());

        if (input.getCapacity() != null) {
            if (input.getCapacity() < 0)
                throw new IllegalArgumentException("Capacity cannot be negative");
            session.setCapacity(input.getCapacity());
        }

        if (input.getStatus() != null && !input.getStatus().trim().isEmpty())
            session.setStatus(input.getStatus().trim());

        return sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        TrainingSession session = getSession(id);
        sessionRepository.delete(session);
    }
}