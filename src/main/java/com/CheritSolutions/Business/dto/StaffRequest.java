package com.CheritSolutions.Business.dto;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class StaffRequest {
    @NotBlank(message = "Staff name is required")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;
     
   @JdbcTypeCode(SqlTypes.JSON) // Use Hibernate's native JSON support
@Column(columnDefinition = "jsonb")
    private JsonNode schedule;

    // Getters and setters
}
