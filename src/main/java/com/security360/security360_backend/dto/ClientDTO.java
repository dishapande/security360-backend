package com.security360.security360_backend.dto;

import java.util.List;

public class ClientDTO {
    private Long id; private String clientName; private String contactPerson;
    private String email; private String phone; private String address;
    private String gstNo; private String panNo; private String paymentStatus;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGstNo() { return gstNo; }
    public void setGstNo(String gstNo) { this.gstNo = gstNo; }
    public String getPanNo() { return panNo; }
    public void setPanNo(String panNo) { this.panNo = panNo; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}