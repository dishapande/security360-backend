package com.security360.security360_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "holidays",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_holiday_date",
            columnNames = "date"
        )
    }
)
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private String name;

    @Column(name = "holiday_type", nullable = false)
    private String holidayType = "Public Holiday";

    public Holiday() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }
}