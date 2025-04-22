package com.CheritSolutions.Business.controller;

import java.time.Instant;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CheritSolutions.Business.dto.AvailableSlotDTO;
import com.CheritSolutions.Business.dto.StaffRequest;
import com.CheritSolutions.Business.dto.StaffResponse;
import com.CheritSolutions.Business.entity.AvailabilitySlot;
import com.CheritSolutions.Business.entity.SlotStatus;
import com.CheritSolutions.Business.entity.Staff;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.repository.AvailabilitySlotRepository;
import com.CheritSolutions.Business.repository.StaffRepository;
import com.CheritSolutions.Business.service.ServiceService;
import com.CheritSolutions.Business.service.StaffService;

@RestController
@RequestMapping("/api/v1/businesses/{businessId}/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;
    
     @Autowired
    private ServiceService serviceService;

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
   // @PreAuthorize("@staffService.isStaffOwner(#staffId, authentication.name)")
    public ResponseEntity<StaffResponse> getStaff(@PathVariable UUID staffId) {
        StaffResponse response = staffService.getStaff(staffId);
        return ResponseEntity.ok(response);
    }

    // Get all staff members for a business
    @GetMapping
  //  @PreAuthorize("hasRole('BUSINESS_OWNER')") 

  public ResponseEntity<List<StaffResponse>> getAllStaffByBusiness(@PathVariable("businessId") UUID businessId) {
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

    
@PostMapping("/{staffId}/assign-service/{serviceId}")
@PreAuthorize("hasRole('BUSINESS_OWNER')")
public ResponseEntity<Void> assignServiceToStaff(
        @PathVariable UUID staffId,
        @PathVariable UUID serviceId) {
    serviceService.assignStaffToService(staffId, serviceId);
    return ResponseEntity.ok().build();
}


// Business Service: StaffController.java
@GetMapping("/{staffId}/availability")
public boolean checkAvailability(
    @PathVariable UUID businessId,
    @PathVariable UUID staffId,
    @RequestParam Instant slotStart,
    @RequestParam Integer duration // Add duration parameter
) {
    return staffService.isStaffAvailable(businessId, staffId, slotStart, duration);
}

@PostMapping("/{staffId}/reserve")
public ResponseEntity<UUID> reserveSlots(
    @PathVariable UUID staffId,
    @RequestParam Instant start,
    @RequestParam int duration) {
    UUID slotId = staffService.reserveSlots(staffId, start, duration); // Adjust service to return slotId
    return ResponseEntity.ok(slotId);
}



@GetMapping("/{staffId}/reservations")
public ResponseEntity<List<AvailabilitySlot>> getReservations(@PathVariable UUID staffId) {
    List<AvailabilitySlot> slots = availabilitySlotRepository
        .findByStaffIdAndStatusIn(staffId, List.of(SlotStatus.BOOKED, SlotStatus.REST));
    return ResponseEntity.ok(slots);
}


@PutMapping("/{staffId}/reservations/{slotId}")
public ResponseEntity<Void> updateReservation(
    @PathVariable UUID staffId, 
    @PathVariable UUID slotId,
    @RequestParam Instant newStart,
    @RequestParam int newDuration
) {
    staffService.updateReservation(staffId, slotId, newStart, newDuration);
    return ResponseEntity.ok().build();
}

@DeleteMapping("/{staffId}/reservations/{slotId}")
public ResponseEntity<Void> cancelReservation(@PathVariable UUID staffId,
                                              @PathVariable UUID slotId) {
    staffService.cancelReservation(staffId, slotId);
    return ResponseEntity.noContent().build();
}








@GetMapping("/{staffId}/available-slots")
public List<AvailableSlotDTO> getAvailableSlots(
    @PathVariable UUID staffId,
    @RequestParam int duration) {
    
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    
    return staffService.generateAvailableSlots(staff, duration);
}



}