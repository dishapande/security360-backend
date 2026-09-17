package com.security360.security360_backend.dto;

import com.security360.security360_backend.entity.PfEsiRecord;

import java.math.BigDecimal;
import java.util.List;

public class PfEsiDashboardResponse {

    private long employeeCount;

    private BigDecimal employeeContribution;

    private BigDecimal employerContribution;

    private long monthlyReturns;

    private long pendingFilings;

    private long pendingChallans;

    private List<PfEsiRecord> records;

    public PfEsiDashboardResponse() {
    }

    public PfEsiDashboardResponse(
            long employeeCount,
            BigDecimal employeeContribution,
            BigDecimal employerContribution,
            long monthlyReturns,
            long pendingFilings,
            long pendingChallans,
            List<PfEsiRecord> records
    ) {
        this.employeeCount = employeeCount;
        this.employeeContribution = employeeContribution;
        this.employerContribution = employerContribution;
        this.monthlyReturns = monthlyReturns;
        this.pendingFilings = pendingFilings;
        this.pendingChallans = pendingChallans;
        this.records = records;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(long employeeCount) {
        this.employeeCount = employeeCount;
    }

    public BigDecimal getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(
            BigDecimal employeeContribution
    ) {
        this.employeeContribution = employeeContribution;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(
            BigDecimal employerContribution
    ) {
        this.employerContribution = employerContribution;
    }

    public long getMonthlyReturns() {
        return monthlyReturns;
    }

    public void setMonthlyReturns(long monthlyReturns) {
        this.monthlyReturns = monthlyReturns;
    }

    public long getPendingFilings() {
        return pendingFilings;
    }

    public void setPendingFilings(long pendingFilings) {
        this.pendingFilings = pendingFilings;
    }

    public long getPendingChallans() {
        return pendingChallans;
    }

    public void setPendingChallans(long pendingChallans) {
        this.pendingChallans = pendingChallans;
    }

    public List<PfEsiRecord> getRecords() {
        return records;
    }

    public void setRecords(List<PfEsiRecord> records) {
        this.records = records;
    }
}