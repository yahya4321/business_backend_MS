package com.CheritSolutions.Business.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CheritSolutions.Business.dto.ServiceRequest;
import com.CheritSolutions.Business.dto.ServiceResponse;
import com.CheritSolutions.Business.entity.Business;
import com.CheritSolutions.Business.entity.Servicee;
import com.CheritSolutions.Business.entity.Staff;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.repository.BusinessRepository;
import com.CheritSolutions.Business.repository.ServiceRepository;
import com.CheritSolutions.Business.repository.StaffRepository;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Create a new service for a business
    @Transactional
    public ServiceResponse createService(UUID businessId, ServiceRequest request) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Servicee service = modelMapper.map(request, Servicee.class);
        service.setBusiness(business);
        Servicee savedService = serviceRepository.save(service);
        return modelMapper.map(savedService, ServiceResponse.class);
    }
    


    // Get a service by ID
    // ServiceService.java
@Transactional(readOnly = true)
public ServiceResponse getService(UUID id) {
    Servicee service = serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

    ServiceResponse response = modelMapper.map(service, ServiceResponse.class);

    // Manually map staff IDs
    if (service.getStaff() != null && !service.getStaff().isEmpty()) {
        response.setStaffIds(
            service.getStaff().stream()
                .map(staff -> staff.getId())
                .collect(Collectors.toList())
        );
    } else {
        response.setStaffIds(new ArrayList<>()); // Avoid null values
    }

    return response;
}

@Transactional(readOnly = true)
public List<ServiceResponse> getAllServicesByBusiness(UUID businessId) {
    List<Servicee> services = serviceRepository.findByBusinessId(businessId);

    return services.stream()
            .map(service -> {
                ServiceResponse response = modelMapper.map(service, ServiceResponse.class);

                // Manually map staff IDs
                if (service.getStaff() != null && !service.getStaff().isEmpty()) {
                    response.setStaffIds(
                        service.getStaff().stream()
                            .map(staff -> staff.getId())
                            .collect(Collectors.toList())
                    );
                } else {
                    response.setStaffIds(new ArrayList<>());
                }

                return response;
            })
            .collect(Collectors.toList());
}

    // Update a service
    @Transactional
    public ServiceResponse updateService(UUID id, ServiceRequest request) {
        Servicee service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        modelMapper.map(request, service); // Update fields from DTO
        Servicee updatedService = serviceRepository.save(service);
        return modelMapper.map(updatedService, ServiceResponse.class);
    }

    // Delete a service
    @Transactional
    public void deleteService(UUID id) {
        Servicee service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        serviceRepository.delete(service);
    }
    public boolean isServiceOwner(UUID serviceId, String userId) {
        return serviceRepository.findById(serviceId)
            .map(service -> service.getBusiness().getOwnerId().equals(userId))
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
    }


    
  // ServiceService.java
public Staff assignStaffToService(UUID staffId, UUID serviceId) {
    Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    Servicee service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

    // Ensure service belongs to the same business as the staff
    if (!staff.getBusiness().getId().equals(service.getBusiness().getId())) {
        throw new IllegalArgumentException("Service does not belong to the staff's business");
    }

    if (staff.getService() != null) {
        staff.getService().getStaff().remove(staff); // Remove from old service's list
    }

    // Assign to new service
    staff.setService(service);
    service.getStaff().add(staff); // Add to new service's list

    return staffRepository.save(staff);
}


public List<ServiceResponse> searchServicesInBusiness(
    UUID businessId, String nameQuery, BigDecimal minPrice, BigDecimal maxPrice) {
    
    List<Servicee> services = serviceRepository.findByBusinessIdWithFilters(
        businessId, nameQuery, minPrice != null ? minPrice : BigDecimal.ZERO, 
        maxPrice != null ? maxPrice : BigDecimal.valueOf(Double.MAX_VALUE));
    
        return services.stream()
        .map(service -> {
            ServiceResponse response = modelMapper.map(service, ServiceResponse.class);

            // Manually map staff IDs
            if (service.getStaff() != null && !service.getStaff().isEmpty()) {
                response.setStaffIds(
                    service.getStaff().stream()
                        .map(staff -> staff.getId())
                        .collect(Collectors.toList())
                );
            } else {
                response.setStaffIds(new ArrayList<>());
            }

            return response;
        })
        .collect(Collectors.toList());
}


public List<ServiceResponse> searchAllServices(
    String nameQuery, BigDecimal minPrice, BigDecimal maxPrice) {
    
    List<Servicee> services = serviceRepository.findAllWithFilters  (
        nameQuery, 
        minPrice != null ? minPrice : BigDecimal.ZERO, 
        maxPrice != null ? maxPrice : BigDecimal.valueOf(Double.MAX_VALUE));
    
        return services.stream()
        .map(service -> {
            ServiceResponse response = modelMapper.map(service, ServiceResponse.class);

            // Manually map staff IDs
            if (service.getStaff() != null && !service.getStaff().isEmpty()) {
                response.setStaffIds(
                    service.getStaff().stream()
                        .map(staff -> staff.getId())
                        .collect(Collectors.toList())
                );
            } else {
                response.setStaffIds(new ArrayList<>());
            }

            return response;
        })
        .collect(Collectors.toList());
}
    
}
