package com.CheritSolutions.Business.controller;

import com.CheritSolutions.Business.dto.ServiceRequest;
import com.CheritSolutions.Business.dto.ServiceResponse;
import com.CheritSolutions.Business.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses/{businessId}/services")
public class ServiceeController {

    @Autowired
    private ServiceService serviceeService;

    // Create a new service for a business
    @PostMapping
   // @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<ServiceResponse> createServicee(
            @PathVariable UUID businessId,
            @RequestBody ServiceRequest request) {
        ServiceResponse response = serviceeService.createService(businessId, request);
        return ResponseEntity.status(201).body(response);
    }

    // Get a service by ID
    @GetMapping("/{serviceId}")
   // @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<ServiceResponse> getServicee(@PathVariable UUID serviceId) {
        ServiceResponse response = serviceeService.getService(serviceId);
        return ResponseEntity.ok(response);
    }

    // Get all services for a business
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServicesByBusiness(@PathVariable UUID businessId) {
        List<ServiceResponse> responses = serviceeService.getAllServicesByBusiness(businessId);
        return ResponseEntity.ok(responses);
    }

    // Update a service
    @PutMapping("/{serviceId}")
   // @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<ServiceResponse> updateServicee(
            @PathVariable UUID serviceId,
            @RequestBody ServiceRequest request) {
        ServiceResponse response = serviceeService.updateService(serviceId, request);
        return ResponseEntity.ok(response);
    }

    // Delete a service
    @DeleteMapping("/{serviceId}")
    //@PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<Void> deleteServicee(@PathVariable UUID serviceId) {
        serviceeService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
}