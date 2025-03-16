package com.CheritSolutions.Business.dto;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private JsonNode schedule; // Match the entity's type
    private UUID businessId;    

    // Getters and setters
}