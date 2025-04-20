package com.CheritSolutions.Business.dto;

import java.util.UUID;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class StaffRequest {
    @NotBlank(message = "Staff name is required")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;
    private JsonNode schedule;

    private UUID serviceId;
    
    @NotNull
    private Integer postBufferTime;
}
