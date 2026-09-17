package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "recruitment_candidates")
public class RecruitmentCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String applicationId;

    @Column(nullable = false)
    public String name;

    public String email;
    public String phone;
    public String role;
    public String branch;

    /* Resume information */
    // 🟢 Accepts BOTH "resumeFileName" and "resume" from React
    @JsonProperty("resume")
    public String resumeFileName;

    public String resumePath;

    /* Recruitment status */
    public String stage;
    public String status;

    /* Interview */
    public LocalDate interviewDate;
    public String interviewer;
    public String interviewResult;

    /* Selection */
    // 🟢 Accepts BOTH "selectionStatus" and "selection" from React
    @JsonProperty("selection")
    public String selectionStatus;

    /* Offer */
    public String offerStatus;
    public LocalDate offerDate;

    /* Joining */
    public LocalDate joiningDate;

    public RecruitmentCandidate() {
    }
}