package com.CheritSolutions.Business.controller;

import com.CheritSolutions.Business.dto.ServiceRequest;
import com.CheritSolutions.Business.dto.ServiceResponse;
import com.CheritSolutions.Business.service.BusinessService;
import com.CheritSolutions.Business.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class ServiceeController {

    @Autowired
    private ServiceService serviceeService;
 

    // Create a new service for a business
    @PostMapping("/api/v1/businesses/{businessId}/services")                              
 //   @PreAuthorize("hasRole('BUSINESS_OWNER') and @businessService.isBusinessOwner(#businessId, authentication.name)")

    public ResponseEntity<ServiceResponse> createServicee(
            @PathVariable UUID businessId,
            @RequestBody ServiceRequest request) {
        ServiceResponse response = serviceeService.createService(businessId, request);
        return ResponseEntity.status(201).body(response);
    }
 
    // Get a service by ID
    @GetMapping("/api/v1/businesses/{businessId}/services/{serviceId}")
   // @PreAuthorize("@serviceService.isServiceOwner(#serviceId, authentication.name)")
    public ResponseEntity<ServiceResponse> getServicee(@PathVariable UUID businessId, @PathVariable UUID serviceId) {
        ServiceResponse response = serviceeService.getService(serviceId);
        if (response == null || !response.getBusinessId().equals(businessId)) {
            return ResponseEntity.notFound().build(); // 404 if service not found or business ID mismatch
        }
        return ResponseEntity.ok(response);
    }

    // Get all services for a business
    @GetMapping("/api/v1/businesses/{businessId}/services") 

    public ResponseEntity<List<ServiceResponse>> getAllServicesByBusiness(@PathVariable UUID businessId) {
        List<ServiceResponse> responses = serviceeService.getAllServicesByBusiness(businessId);
        return ResponseEntity.ok(responses);
    }

    // Update a service
    @PutMapping("/api/v1/businesses/{businessId}/services/{serviceId}")
    //@PreAuthorize("@serviceService.isServiceOwner(#serviceId, authentication.name)")
    public ResponseEntity<ServiceResponse> updateServicee(
            @PathVariable UUID serviceId,
            @RequestBody ServiceRequest request) {
        ServiceResponse response = serviceeService.updateService(serviceId, request);
        return ResponseEntity.ok(response);
    }

    // Delete a service
    @DeleteMapping("/api/v1/businesses/{businessId}/services/{serviceId}")
  //  @PreAuthorize("@serviceService.isServiceOwner(#serviceId, authentication.name)")
    public ResponseEntity<Void> deleteServicee(@PathVariable UUID serviceId) {
        serviceeService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/api/v1/businesses/{businessId}/services/{serviceId}/assign-staff/{staffId}")
    public ResponseEntity<Void> assignStaffToService(
            @PathVariable UUID serviceId,
            @PathVariable UUID staffId) {
        serviceeService.assignStaffToService(staffId, serviceId); // Fixed parameter order
        return ResponseEntity.ok().build();
    }


    @GetMapping("/api/v1/businesses/{businessId}/services/search")
public ResponseEntity<List<ServiceResponse>> searchServicesInBusiness(
    @PathVariable UUID businessId,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice) {
    
    List<ServiceResponse> results = serviceeService.searchServicesInBusiness(
        businessId, name, minPrice, maxPrice);
    return ResponseEntity.ok(results);
}


@GetMapping("/api/v1/businesses/services/search")
public ResponseEntity<List<ServiceResponse>> searchAllServices(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice) {
    
    List<ServiceResponse> results = serviceeService.searchAllServices(name, minPrice, maxPrice);
    return ResponseEntity.ok(results);
}
}