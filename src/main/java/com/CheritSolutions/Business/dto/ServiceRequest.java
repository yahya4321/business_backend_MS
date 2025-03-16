package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
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

  @JdbcTypeCode(SqlTypes.JSON) // Use Hibernate's native JSON support
@Column(columnDefinition = "jsonb")
    private JsonNode pricingRules;

    // Getters and setters
}