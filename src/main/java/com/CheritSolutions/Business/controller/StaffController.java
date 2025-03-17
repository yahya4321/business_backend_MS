package com.CheritSolutions.Business.controller;

import com.CheritSolutions.Business.dto.StaffRequest;
import com.CheritSolutions.Business.dto.StaffResponse;
import com.CheritSolutions.Business.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses/{businessId}/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Add a staff member to a business
    @PostMapping
  //  @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<StaffResponse> addStaff(
            @PathVariable UUID businessId,
            @RequestBody StaffRequest request) {
        StaffResponse response = staffService.createStaff(businessId, request);
        return ResponseEntity.status(201).body(response);
    }

    // Get a staff member by ID
    @GetMapping("/{staffId}")
    @PreAuthorize("@staffService.isStaffOwner(#staffId, authentication.name)")
    public ResponseEntity<StaffResponse> getStaff(@PathVariable UUID staffId) {
        StaffResponse response = staffService.getStaff(staffId);
        return ResponseEntity.ok(response);
    }

    // Get all staff members for a business
    @GetMapping
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 

    public ResponseEntity<List<StaffResponse>> getAllStaffByBusiness(@PathVariable UUID businessId) {
        List<StaffResponse> responses = staffService.getAllStaffByBusiness(businessId);
        return ResponseEntity.ok(responses);
    }

    // Update a staff member
    @PutMapping("/{staffId}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable UUID staffId,
            @RequestBody StaffRequest request) {
        StaffResponse response = staffService.updateStaff(staffId, request);
        return ResponseEntity.ok(response);
    }

    // Delete a staff member
    @DeleteMapping("/{staffId}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") 
    public ResponseEntity<Void> deleteStaff(@PathVariable UUID staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}