package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.HousekeepingIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousekeepingIssueRepository extends JpaRepository<HousekeepingIssue, Long> {
}