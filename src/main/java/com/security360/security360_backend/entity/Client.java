package com.security360.security360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "client_name", nullable = false) private String clientName;
    @Column(name = "contact_person") private String contactPerson;
    private String email; private String phone; private String address;
    @Column(name = "gst_no") private String gstNo;
    @Column(name = "pan_no") private String panNo;
    @Column(name = "payment_status") private String paymentStatus;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
    
    // Getters & Setters
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