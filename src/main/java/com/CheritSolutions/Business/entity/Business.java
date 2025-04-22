package com.CheritSolutions.Business.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use UUID for distributed systems
    private UUID id;

    @Column(nullable = false, unique = true) // Ensure business names are unique
    private String name;

    @Column(nullable = false)
    private String address;
    
    @Column(name = "owner_id")
    private String ownerId;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Servicee> getServices() {
        return this.services;
    }

    public void setServices(List<Servicee> services) {
        this.services = services;
    }

    public List<Staff> getStaffMembers() {
        return this.staffMembers;
    }

    public void setStaffMembers(List<Staff> staffMembers) {
        this.staffMembers = staffMembers;
    }

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Avoid circular references in JSON responses
    private List<Servicee> services = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Staff> staffMembers = new ArrayList<>();

    // Getters, setters, and helper methods
}