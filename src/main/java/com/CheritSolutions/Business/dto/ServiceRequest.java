package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServiceRequest {
    @NotBlank(message = "Service name is required")
    private String name;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal basePrice;
    private JsonNode pricingRules;
    @NotNull
    private Integer duration;


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

  public JsonNode getPricingRules() {
    return this.pricingRules;
  }

  public void setPricingRules(JsonNode pricingRules) {
    this.pricingRules = pricingRules;
  }

  public Integer getDuration() {
    return this.duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  }