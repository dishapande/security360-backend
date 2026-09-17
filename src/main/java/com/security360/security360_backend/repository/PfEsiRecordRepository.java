package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.PfEsiRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfEsiRecordRepository
        extends JpaRepository<PfEsiRecord, Long> {

    List<PfEsiRecord> findByTypeIgnoreCase(String type);

    List<PfEsiRecord> findByStatusIgnoreCase(String status);

    List<PfEsiRecord> findByTypeIgnoreCaseAndStatusIgnoreCase(
            String type,
            String status
    );

    List<PfEsiRecord> findByEmployeeCodeIgnoreCase(
            String employeeCode
    );
}