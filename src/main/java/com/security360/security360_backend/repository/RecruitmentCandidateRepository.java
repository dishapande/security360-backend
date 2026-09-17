package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.RecruitmentCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentCandidateRepository
        extends JpaRepository<RecruitmentCandidate, Long> {

    List<RecruitmentCandidate>
    findByNameContainingIgnoreCase(String name);

    List<RecruitmentCandidate>
    findByStageIgnoreCase(String stage);

    List<RecruitmentCandidate>
    findByStatusIgnoreCase(String status);

    List<RecruitmentCandidate>
    findByRoleContainingIgnoreCase(String role);

    boolean existsByApplicationId(String applicationId);
}