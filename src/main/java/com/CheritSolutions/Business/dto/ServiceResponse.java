package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class ServiceResponse {
    private UUID id;
    private String name;
    private BigDecimal basePrice; // Calculated from base_price + pricing_rules
    private UUID businessId;
    private Integer duration;
    
    private JsonNode pricingRules; // Simplified JSON for clients
    private List<UUID> staffIds; // Populated from staff list IDs
}
