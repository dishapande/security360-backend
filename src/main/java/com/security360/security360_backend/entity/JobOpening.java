package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_openings")
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String jobCode;

    @Column(nullable = false)
    public String title;

    public String department;

    public String branch;

    public Integer vacancies;

    public LocalDate openingDate;

    public LocalDate closingDate;

    public String status;

    public JobOpening() {
    }
}