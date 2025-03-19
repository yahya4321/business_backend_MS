package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;


import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class ServiceRequest {
    @NotBlank(message = "Service name is required")
    private String name;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal basePrice;
    private JsonNode pricingRules;


  }