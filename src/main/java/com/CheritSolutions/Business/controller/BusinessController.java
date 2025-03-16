package com.CheritSolutions.Business.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CheritSolutions.Business.dto.BusinessRequest;
import com.CheritSolutions.Business.dto.BusinessResponse;
import com.CheritSolutions.Business.service.BusinessService;

@RestController
@RequestMapping("/api/v1/businesses")

public class BusinessController {

    @Autowired
    private BusinessService businessService;

    // Create a new business
    @PostMapping
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<BusinessResponse> createBusiness( @RequestBody BusinessRequest request) {
        BusinessResponse response = businessService.createBusiness(request);
        return ResponseEntity.status(201).body(response);
    }

    // Get a business by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<BusinessResponse> getBusiness(@PathVariable UUID id) {
        BusinessResponse response = businessService.getBusiness(id);
        return ResponseEntity.ok(response);
    }

    // Get all businesses
    @GetMapping
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 

    public ResponseEntity<List<BusinessResponse>> getAllBusinesses() {
        List<BusinessResponse> responses = businessService.getAllBusinesses();
        return ResponseEntity.ok(responses);
    }

    // Update a business
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<BusinessResponse> updateBusiness(@PathVariable UUID id, @RequestBody BusinessRequest request) {
        BusinessResponse response = businessService.updateBusiness(id, request);
        return ResponseEntity.ok(response);
    }

    // Delete a business
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<Void> deleteBusiness(@PathVariable UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}