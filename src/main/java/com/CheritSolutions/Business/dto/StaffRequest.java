package com.CheritSolutions.Business.dto;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StaffRequest {
    // Fields
    @NotBlank(message = "Staff name is required")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;
    private JsonNode schedule;
    private UUID serviceId;
    
    @NotNull
    private Integer postBufferTime;

    // Getters and Setters (INSIDE the class)
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

    public JsonNode getSchedule() {
        return this.schedule;
    }

    public void setSchedule(JsonNode schedule) {
        this.schedule = schedule;
    }

    public UUID getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getPostBufferTime() {
        return this.postBufferTime;
    }

    public void setPostBufferTime(Integer postBufferTime) {
        this.postBufferTime = postBufferTime;
    }
}