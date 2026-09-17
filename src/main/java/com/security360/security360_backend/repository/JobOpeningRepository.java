package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOpeningRepository
        extends JpaRepository<JobOpening, Long> {

    List<JobOpening> findByStatusIgnoreCase(String status);

    List<JobOpening> findByTitleContainingIgnoreCase(String title);

    boolean existsByJobCode(String jobCode);
}