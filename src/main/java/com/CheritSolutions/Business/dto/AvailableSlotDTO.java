package com.CheritSolutions.Business.dto;

import java.time.Instant;

import com.CheritSolutions.Business.entity.AvailabilitySlot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor // Add this
@AllArgsConstructor
@Getter
public class AvailableSlotDTO {
    private Instant startTime;
    private Instant endTime;
    private String status;

    // Constructor for generated available slots
    public AvailableSlotDTO(Instant startTime, Instant endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "AVAILABLE"; // Default status for generated slots
    }

    // Constructor for existing AvailabilitySlot entities
    public AvailableSlotDTO(AvailabilitySlot slot) {
        this.startTime = slot.getStartTime();
        this.endTime = slot.getEndTime();
        this.status = slot.getStatus().name(); // Direct enum conversion
    }

  
}