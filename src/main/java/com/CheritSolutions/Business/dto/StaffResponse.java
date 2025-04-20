package com.CheritSolutions.Business.dto;

import java.util.List;
import java.util.UUID;

import com.CheritSolutions.Business.entity.Business;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter 
public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private Integer postBufferTime;
    private Business business;
    private JsonNode schedule;
    private List<AvailableSlotDTO> availabilitySlots; 
    private UUID serviceId; 

    
}