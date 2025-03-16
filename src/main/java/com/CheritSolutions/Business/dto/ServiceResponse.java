package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class ServiceResponse {
    private UUID id;
    private String name;
    private BigDecimal basePrice; // Calculated from base_price + pricing_rules
     @JdbcTypeCode(SqlTypes.JSON) // Use Hibernate's native JSON support
@Column(columnDefinition = "jsonb")
    private JsonNode pricingRules; // Simplified JSON for clients
}
