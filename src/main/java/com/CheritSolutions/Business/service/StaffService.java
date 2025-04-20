package com.CheritSolutions.Business.service;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CheritSolutions.Business.dto.AvailableSlotDTO;
import com.CheritSolutions.Business.dto.StaffRequest;
import com.CheritSolutions.Business.dto.StaffResponse;
import com.CheritSolutions.Business.entity.AvailabilitySlot;
import com.CheritSolutions.Business.entity.Business;
import com.CheritSolutions.Business.entity.SlotStatus;
import com.CheritSolutions.Business.entity.Staff;
import com.CheritSolutions.Business.exception.ResourceNotFoundException;
import com.CheritSolutions.Business.exception.SlotConflictException;
import com.CheritSolutions.Business.repository.AvailabilitySlotRepository;
import com.CheritSolutions.Business.repository.BusinessRepository;
import com.CheritSolutions.Business.repository.StaffRepository;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;


    @Autowired
    private ServiceService serviceService;
    

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public StaffResponse createStaff(UUID businessId, StaffRequest request) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
    
        Staff staff = modelMapper.map(request, Staff.class);
        staff.setBusiness(business);
        staff.setPostBufferTime(request.getPostBufferTime()); 
    
        Staff savedStaff = staffRepository.save(staff);
    
        if (request.getServiceId() != null) {
            serviceService.assignStaffToService(savedStaff.getId(), request.getServiceId());
        }
    
        return modelMapper.map(savedStaff, StaffResponse.class);
    }
    

    // Get a staff member by ID
    @Transactional(readOnly = true)
    public StaffResponse getStaff(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return modelMapper.map(staff, StaffResponse.class);
    }

    // Get all staff members for a business
    @Transactional(readOnly = true)
    public List<StaffResponse> getAllStaffByBusiness(UUID businessId) {
        List<Staff> staffMembers = staffRepository.findByBusinessId(businessId);
        return staffMembers.stream()
                .map(staff -> modelMapper.map(staff, StaffResponse.class))
                .collect(Collectors.toList());
    }

    // Update a staff member
    @Transactional
    public StaffResponse updateStaff(UUID id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        modelMapper.map(request, staff); // Update fields from DTO
        Staff updatedStaff = staffRepository.save(staff);
        return modelMapper.map(updatedStaff, StaffResponse.class);
    }

    // Delete a staff member
    @Transactional
    public void deleteStaff(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        staffRepository.delete(staff);
    }


    


    public boolean isStaffOwner(UUID staffId, String userId) {
        return staffRepository.findById(staffId)
            .map(staff -> staff.getBusiness().getOwnerId().equals(userId))
            .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));
    }


// StaffService.java
public boolean isStaffAvailable(UUID businessId, UUID staffId, Instant slotStart, Integer duration) {
    Staff staff = staffRepository.findByIdAndBusinessId(staffId, businessId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    
    // 1. Check future date
    if (slotStart.isBefore(Instant.now())) {
        return false;
    }
    
    // 2. Check working hours
    if (!staff.isWithinWorkingHours(slotStart)) {
        return false;
    }
    
    // 3. Check buffer time and existing slots
    Instant bufferEnd = staff.getBufferEndTime(slotStart, duration);
    List<AvailabilitySlot> conflicts = availabilitySlotRepository.findByStaffIdAndOverlap(
        staffId, slotStart, bufferEnd);
    
    return conflicts.isEmpty();
}


@Transactional
public UUID reserveSlots(UUID staffId, Instant start, int duration) {
    // Fetch staff and validate existence
    Staff staff = staffRepository.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

    // Calculate the end time using buffer time
    Instant end = staff.getBufferEndTime(start, duration);
    
    // Check for conflicts with the slots (optimized with lock and index)
    List<AvailabilitySlot> conflicts = availabilitySlotRepository.findByStaffIdAndOverlapWithLock(
        staffId, start, end);
    
    if (!conflicts.isEmpty()) {
        throw new SlotConflictException("Slot conflict detected");
    }
    
    // Create both the "BOOKED" and "REST" slots
    AvailabilitySlot booked = createSlot(staff, start, duration, SlotStatus.BOOKED);
    AvailabilitySlot rest = createSlot(staff, booked.getEndTime(), 
                                       Duration.between(booked.getEndTime(), end).toMinutes(), SlotStatus.REST);
    
    // Save the slots in one batch operation and return the booked slot's ID
    List<AvailabilitySlot> savedSlots = availabilitySlotRepository.saveAll(List.of(booked, rest));
    return savedSlots.get(0).getId(); // Return the UUID of the BOOKED slot
}


@Transactional
public void updateReservation(UUID staffId, UUID slotId, Instant newStart, int newDuration) {
    AvailabilitySlot slot = availabilitySlotRepository.findById(slotId)
        .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

    if (!slot.getStaff().getId().equals(staffId)) {
        throw new IllegalArgumentException("Slot does not belong to this staff");
    }

    // Delete old slot + rest
    availabilitySlotRepository.deleteByStaffIdAndStartTime(staffId, slot.getStartTime());

    // Re-reserve using new time
    reserveSlots(staffId, newStart, newDuration);
}

@Transactional
public void cancelReservation(UUID staffId, UUID slotId) {
    AvailabilitySlot booked = availabilitySlotRepository.findById(slotId)
        .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

    if (!booked.getStaff().getId().equals(staffId)) {
        throw new IllegalArgumentException("Slot does not belong to this staff");
    }

    // Delete the BOOKED slot and associated REST slot
    availabilitySlotRepository.deleteAllByStaffIdAndStartTimeIn(
        staffId, List.of(booked.getStartTime(), booked.getEndTime()));
}



// Helper method to create a slot
private AvailabilitySlot createSlot(Staff staff, Instant start, long duration, SlotStatus status) {
    return AvailabilitySlot.builder()
        .staff(staff)
        .startTime(start)
        .endTime(start.plus(duration, ChronoUnit.MINUTES))
        .status(status)
        .build();
}







public List<AvailableSlotDTO> generateAvailableSlots(Staff staff, int serviceDuration) {
    // Calculate the total required time (service + post buffer time)
    int totalRequiredTime = serviceDuration + staff.getPostBufferTime();
    
    // Fetch all booked slots for the staff in advance to minimize DB calls
    List<AvailabilitySlot> bookedSlots = availabilitySlotRepository
        .findByStaffIdAndStatusIn(staff.getId(), List.of(SlotStatus.BOOKED, SlotStatus.REST));

    // Schedule data for the staff
    JsonNode schedule = staff.getSchedule();
    List<AvailableSlotDTO> availableSlots = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    
    // Preprocess and cache the day names and their working hours to avoid repeated parsing
    Map<String, LocalDateTime[]> workingHoursMap = new HashMap<>();
    
    // Loop through the next 7 days
    for (int day = 0; day < 7; day++) {
        // Calculate the start of the day at 00:00
        LocalDateTime dayStart = now.plusDays(day).withHour(0).withMinute(0);
        String dayName = dayStart.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
        
        // Skip days that are closed
        JsonNode daySchedule = schedule.get(dayName);
        if (daySchedule == null || daySchedule.asText().equals("Closed")) continue;

        // Retrieve and parse the working hours (only once per day)
        if (!workingHoursMap.containsKey(dayName)) {
            String[] hours = daySchedule.asText().split(" - ");
            LocalTime openTime = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern("h:mm a"));
            LocalTime closeTime = LocalTime.parse(hours[1], DateTimeFormatter.ofPattern("h:mm a"));
            LocalDateTime[] times = new LocalDateTime[]{dayStart.with(openTime), dayStart.with(closeTime)};
            workingHoursMap.put(dayName, times);
        }
        
        LocalDateTime[] workingHours = workingHoursMap.get(dayName);
        LocalDateTime dayOpen = workingHours[0];
        LocalDateTime dayClose = workingHours[1];

        // Generate slots starting from the opening time
        LocalDateTime slotStart = dayOpen;
        while (slotStart.plusMinutes(totalRequiredTime).isBefore(dayClose)) {
            // Calculate the end time of the current slot
            LocalDateTime slotEnd = slotStart.plusMinutes(totalRequiredTime);
            
            // Convert to Instant for comparison
            Instant startInstant = slotStart.atZone(ZoneId.systemDefault()).toInstant();
            Instant endInstant = slotEnd.atZone(ZoneId.systemDefault()).toInstant();

            // Check for conflicts with booked slots using more efficient filtering
            boolean hasConflict = bookedSlots.stream().anyMatch(booked ->
                !(booked.getEndTime().isBefore(startInstant) || booked.getStartTime().isAfter(endInstant))
            );

            // If no conflict, add the slot to the list
            if (!hasConflict) {
                availableSlots.add(new AvailableSlotDTO(startInstant, endInstant));
            }

            // Move to the next slot (by incrementing by the service duration, not 1 minute)
            slotStart = slotStart.plusMinutes(serviceDuration); // Increment by serviceDuration instead of 1 minute
        }
    }

    return availableSlots;
}

}
