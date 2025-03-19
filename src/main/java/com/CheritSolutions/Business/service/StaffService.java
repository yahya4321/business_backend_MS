package com.CheritSolutions.Business.service;
import com.CheritSolutions.Business.dto.StaffRequest;
import com.CheritSolutions.Business.dto.StaffResponse;
import com.CheritSolutions.Business.entity.Business;
import com.CheritSolutions.Business.entity.Servicee;
import com.CheritSolutions.Business.entity.Staff;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.repository.BusinessRepository;
import com.CheritSolutions.Business.repository.ServiceRepository;
import com.CheritSolutions.Business.repository.StaffRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;


@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private ServiceService serviceService;
    

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
public StaffResponse createStaff(UUID businessId, StaffRequest request) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
    
    Staff staff = modelMapper.map(request, Staff.class);
    staff.setBusiness(business);
    Staff savedStaff = staffRepository.save(staff);

    // Assign to service if specified
    if (request.getServiceId() != null) {
        serviceService.assignStaffToService(savedStaff.getId(), request.getServiceId());
    }

    return modelMapper.map(staffRepository.findById(savedStaff.getId()).orElse(null), StaffResponse.class);
}

    // Get a staff member by ID
    @Transactional(readOnly = true)
    public StaffResponse getStaff(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return modelMapper.map(staff, StaffResponse.class);
    }

    // Get all staff members for a business
    @Transactional(readOnly = true)
    public List<StaffResponse> getAllStaffByBusiness(UUID businessId) {
        List<Staff> staffMembers = staffRepository.findByBusinessId(businessId);
        return staffMembers.stream()
                .map(staff -> modelMapper.map(staff, StaffResponse.class))
                .collect(Collectors.toList());
    }

    // Update a staff member
    @Transactional
    public StaffResponse updateStaff(UUID id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        modelMapper.map(request, staff); // Update fields from DTO
        Staff updatedStaff = staffRepository.save(staff);
        return modelMapper.map(updatedStaff, StaffResponse.class);
    }

    // Delete a staff member
    @Transactional
    public void deleteStaff(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        staffRepository.delete(staff);
    }


    


    public boolean isStaffOwner(UUID staffId, String userId) {
        return staffRepository.findById(staffId)
            .map(staff -> staff.getBusiness().getOwnerId().equals(userId))
            .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));
    }
}
