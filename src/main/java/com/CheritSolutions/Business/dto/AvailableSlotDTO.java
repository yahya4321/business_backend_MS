package com.CheritSolutions.Business.dto;

import java.time.Instant;

import com.CheritSolutions.Business.entity.AvailabilitySlot;

public class AvailableSlotDTO {
    private Instant startTime;
    private Instant endTime;
    private String status;

    // Constructor for generated available slots
    public AvailableSlotDTO(Instant startTime, Instant endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "AVAILABLE";
    }

    // Constructor for existing AvailabilitySlot entities
    public AvailableSlotDTO(AvailabilitySlot slot) {
        this.startTime = slot.getStartTime();
        this.endTime = slot.getEndTime();
        this.status = slot.getStatus().name();
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public AvailableSlotDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
