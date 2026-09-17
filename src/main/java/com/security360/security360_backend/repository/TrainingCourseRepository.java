package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.TrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingCourseRepository
        extends JpaRepository<TrainingCourse, Long> {

    Optional<TrainingCourse> findByCourseCode(String courseCode);

    List<TrainingCourse> findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String name,
            String instructor,
            String category
    );
}