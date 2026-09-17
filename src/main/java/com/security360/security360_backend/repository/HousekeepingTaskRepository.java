package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.HousekeepingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousekeepingTaskRepository extends JpaRepository<HousekeepingTask, Long> {
    List<HousekeepingTask> findAllByOrderByCreatedAtDesc();
    List<HousekeepingTask> findByStaff_Id(Long staffId);
    long countByStatus(String status);
}