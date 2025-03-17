package com.CheritSolutions.Business.service;
import com.CheritSolutions.Business.dto.ServiceRequest;
import com.CheritSolutions.Business.dto.ServiceResponse;
import com.CheritSolutions.Business.entity.Business;
import com.CheritSolutions.Business.entity.Servicee;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.repository.BusinessRepository;
import com.CheritSolutions.Business.repository.ServiceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

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
    @Transactional(readOnly = true)
    public ServiceResponse getService(UUID id) {
        Servicee service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        return modelMapper.map(service, ServiceResponse.class);
    }

    // Get all services for a business
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServicesByBusiness(UUID businessId) {
        List<Servicee> services = serviceRepository.findByBusinessId(businessId);
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceResponse.class))
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
}
