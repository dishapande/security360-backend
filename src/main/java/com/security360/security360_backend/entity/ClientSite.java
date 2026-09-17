package com.security360.security360_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "client_sites")
public class ClientSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String siteName;

    @Column(length = 300)
    private String location;

    @Column(length = 500)
    private String address;

    @Column(length = 30)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}