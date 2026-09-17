package com.security360.security360_backend.service;

import com.security360.security360_backend.dto.PfEsiDashboardResponse;
import com.security360.security360_backend.entity.PfEsiRecord;
import com.security360.security360_backend.repository.PfEsiRecordRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PfEsiService {

    private final PfEsiRecordRepository repository;

    public PfEsiService(PfEsiRecordRepository repository) {
        this.repository = repository;
    }

    /*
     * Get all records
     */
    public List<PfEsiRecord> getAll() {
        return repository.findAll();
    }

    /*
     * Get dashboard
     */
    public PfEsiDashboardResponse getDashboard() {

        List<PfEsiRecord> records = repository.findAll();

        long employeeCount = records.stream()
                .map(PfEsiRecord::getEmployeeCode)
                .filter(code -> code != null && !code.isBlank())
                .distinct()
                .count();

        BigDecimal employeeContribution =
                records.stream()
                        .map(PfEsiRecord::getEmployeeContribution)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal employerContribution =
                records.stream()
                        .map(PfEsiRecord::getEmployerContribution)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        long monthlyReturns = records.stream()
                .filter(record ->
                        "MONTHLY_RETURN".equalsIgnoreCase(
                                record.getType()
                        )
                )
                .count();

        long pendingFilings = records.stream()
                .filter(record ->
                        "PENDING".equalsIgnoreCase(
                                record.getStatus()
                        )
                )
                .filter(record ->
                        "MONTHLY_RETURN".equalsIgnoreCase(
                                record.getType()
                        )
                        ||
                        "COMPLIANCE".equalsIgnoreCase(
                                record.getType()
                        )
                )
                .count();

        long pendingChallans = records.stream()
                .filter(record ->
                        "PENDING".equalsIgnoreCase(
                                record.getStatus()
                        )
                )
                .filter(record ->
                        "PF_CHALLAN".equalsIgnoreCase(
                                record.getType()
                        )
                        ||
                        "ESI_CHALLAN".equalsIgnoreCase(
                                record.getType()
                        )
                )
                .count();

        return new PfEsiDashboardResponse(
                employeeCount,
                employeeContribution,
                employerContribution,
                monthlyReturns,
                pendingFilings,
                pendingChallans,
                records
        );
    }

    /*
     * Get single record
     */
    public PfEsiRecord getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PF / ESI record not found with id: " + id
                        )
                );
    }

    /*
     * Create
     */
    public PfEsiRecord create(PfEsiRecord record) {

        validateRecord(record);

        record.setId(null);

        return repository.save(record);
    }

    /*
     * Update
     */
    public PfEsiRecord update(
            Long id,
            PfEsiRecord updatedRecord
    ) {

        PfEsiRecord existing = getById(id);

        existing.setEmployeeCode(
                updatedRecord.getEmployeeCode()
        );

        existing.setEmployeeName(
                updatedRecord.getEmployeeName()
        );

        existing.setUan(
                updatedRecord.getUan()
        );

        existing.setEsicNumber(
                updatedRecord.getEsicNumber()
        );

        existing.setMonth(
                updatedRecord.getMonth()
        );

        existing.setEmployeeContribution(
                updatedRecord.getEmployeeContribution()
        );

        existing.setEmployerContribution(
                updatedRecord.getEmployerContribution()
        );

        existing.setType(
                updatedRecord.getType()
        );

        existing.setStatus(
                updatedRecord.getStatus()
        );

        existing.setChallanNumber(
                updatedRecord.getChallanNumber()
        );

        existing.setReturnNumber(
                updatedRecord.getReturnNumber()
        );

        existing.setDueDate(
                updatedRecord.getDueDate()
        );

        existing.setFilingDate(
                updatedRecord.getFilingDate()
        );

        existing.setRemarks(
                updatedRecord.getRemarks()
        );

        validateRecord(existing);

        return repository.save(existing);
    }

    /*
     * Delete
     */
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "PF / ESI record not found with id: " + id
            );
        }

        repository.deleteById(id);
    }

    /*
     * Filter by type
     */
    public List<PfEsiRecord> getByType(String type) {

        if (type == null || type.isBlank()) {
            return repository.findAll();
        }

        return repository.findByTypeIgnoreCase(type);
    }

    /*
     * Filter by status
     */
    public List<PfEsiRecord> getByStatus(String status) {

        if (status == null || status.isBlank()) {
            return repository.findAll();
        }

        return repository.findByStatusIgnoreCase(status);
    }

    /*
     * Validation
     */
    private void validateRecord(PfEsiRecord record) {

        if (record.getType() == null ||
                record.getType().isBlank()) {

            throw new IllegalArgumentException(
                    "PF / ESI type is required"
            );
        }

        if (record.getStatus() == null ||
                record.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "PF / ESI status is required"
            );
        }

        if (record.getEmployeeContribution() == null) {
            record.setEmployeeContribution(
                    BigDecimal.ZERO
            );
        }

        if (record.getEmployerContribution() == null) {
            record.setEmployerContribution(
                    BigDecimal.ZERO
            );
        }
    }
}