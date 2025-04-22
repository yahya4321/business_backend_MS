package com.CheritSolutions.Business.entity;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false)
    private String name;

  

    @Column(nullable = false)
    private String position;


    @Column(nullable = false)
    private JsonNode schedule;

    @Column(name = "post_buffer_time", nullable = false)
    private Integer postBufferTime; // Adjustable buffer time in minutes

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();



    @Column(name = "average_rating", precision = 3, scale = 1)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;



    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "service_id", nullable = true) // Can be null if staff is not assigned
private Servicee service;
public UUID getId() {
    return this.id;
}

public void setId(UUID id) {
    this.id = id;
}

public Business getBusiness() {
    return this.business;
}

public void setBusiness(Business business) {
    this.business = business;
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

public JsonNode getSchedule() {
    return this.schedule;
}

public void setSchedule(JsonNode schedule) {
    this.schedule = schedule;
}

public Integer getPostBufferTime() {
    return this.postBufferTime;
}

public void setPostBufferTime(Integer postBufferTime) {
    this.postBufferTime = postBufferTime;
}

public List<AvailabilitySlot> getAvailabilitySlots() {
    return this.availabilitySlots;
}

public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
    this.availabilitySlots = availabilitySlots;
}

public Servicee getService() {
    return this.service;
}

public void setService(Servicee service) {
    this.service = service;
}
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

// Add helper method to calculate buffer end time
public Instant getBufferEndTime(Instant startTime, int duration) {
    return startTime.plus(duration + this.postBufferTime, ChronoUnit.MINUTES);
}


public boolean isWithinWorkingHours(Instant time) {
    LocalDateTime localTime = time.atZone(ZoneId.systemDefault()).toLocalDateTime();
    DayOfWeek day = localTime.getDayOfWeek();
    String dayName = day.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
    
    JsonNode scheduleNode = this.schedule.get(dayName);
    if (scheduleNode == null || scheduleNode.asText().equals("Closed")) {
        return false;
    }
    
    String[] hours = scheduleNode.asText().split(" - ");
    LocalTime start = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern("h:mm a"));
    LocalTime end = LocalTime.parse(hours[1], DateTimeFormatter.ofPattern("h:mm a"));
    
    return !localTime.toLocalTime().isBefore(start) && 
           !localTime.toLocalTime().isAfter(end);
}
}
