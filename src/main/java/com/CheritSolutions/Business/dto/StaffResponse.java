package com.CheritSolutions.Business.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.CheritSolutions.Business.entity.Business;
import com.fasterxml.jackson.databind.JsonNode;


public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private Integer postBufferTime;
    private Business business;
    private JsonNode schedule;
    private List<AvailableSlotDTO> availabilitySlots; 
    private UUID serviceId; 
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

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getPostBufferTime() {
        return this.postBufferTime;
    }

    public void setPostBufferTime(Integer postBufferTime) {
        this.postBufferTime = postBufferTime;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public JsonNode getSchedule() {
        return this.schedule;
    }

    public void setSchedule(JsonNode schedule) {
        this.schedule = schedule;
    }

    public List<AvailableSlotDTO> getAvailabilitySlots() {
        return this.availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailableSlotDTO> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

    public UUID getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    
}