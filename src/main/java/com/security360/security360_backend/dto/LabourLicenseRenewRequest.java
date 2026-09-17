package com.security360.security360_backend.dto;

import java.time.LocalDate;

public class LabourLicenseRenewRequest {

    private LocalDate renewalDate;
    private LocalDate newExpiryDate;

    public LabourLicenseRenewRequest() {
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public LocalDate getNewExpiryDate() {
        return newExpiryDate;
    }

    public void setNewExpiryDate(LocalDate newExpiryDate) {
        this.newExpiryDate = newExpiryDate;
    }
}