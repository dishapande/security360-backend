package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.Door;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoorRepository extends JpaRepository<Door, Long> {

    // Fetch all doors sorted by newest first
    List<Door> findAllByOrderByCreatedAtDesc();
}