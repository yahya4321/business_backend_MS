package com.CheritSolutions.Business.service;

import com.CheritSolutions.Business.dto.BusinessRequest;
import com.CheritSolutions.Business.dto.BusinessResponse;
import com.CheritSolutions.Business.entity.Business;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.repository.BusinessRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Create a new business
    @Transactional
    public BusinessResponse createBusiness(BusinessRequest request) {
        Business business = modelMapper.map(request, Business.class);
        Business savedBusiness = businessRepository.save(business);
        return modelMapper.map(savedBusiness, BusinessResponse.class);
    }

    // Get a business by ID
    @Transactional(readOnly = true)
    public BusinessResponse getBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        return modelMapper.map(business, BusinessResponse.class);
    }

    // Get all businesses
    @Transactional(readOnly = true)
    public List<BusinessResponse> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream()
                .map(business -> modelMapper.map(business, BusinessResponse.class))
                .collect(Collectors.toList());
    }

    // Update a business
    @Transactional
    public BusinessResponse updateBusiness(UUID id, BusinessRequest request) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        modelMapper.map(request, business); // Update fields from DTO
        Business updatedBusiness = businessRepository.save(business);
        return modelMapper.map(updatedBusiness, BusinessResponse.class);
    }

    // Delete a business
    @Transactional
    public void deleteBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        businessRepository.delete(business);
    }
}