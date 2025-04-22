package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;


public class ServiceResponse {
    private UUID id;
    private String name;
    private BigDecimal basePrice; // Calculated from base_price + pricing_rules
    private UUID businessId;
    private Integer duration;
    
    private JsonNode pricingRules; // Simplified JSON for clients
    private List<UUID> staffIds; // Populated from staff list IDs
    private BigDecimal averageRating;
    private Integer ratingCount;

    public BigDecimal getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return this.ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public UUID getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public JsonNode getPricingRules() {
        return this.pricingRules;
    }

    public void setPricingRules(JsonNode pricingRules) {
        this.pricingRules = pricingRules;
    }

    public List<UUID> getStaffIds() {
        return this.staffIds;
    }

    public void setStaffIds(List<UUID> staffIds) {
        this.staffIds = staffIds;
    }
}

  
