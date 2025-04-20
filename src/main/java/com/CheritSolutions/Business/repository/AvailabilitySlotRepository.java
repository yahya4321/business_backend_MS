package com.CheritSolutions.Business.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Business.entity.AvailabilitySlot;
import com.CheritSolutions.Business.entity.SlotStatus;

import jakarta.persistence.LockModeType;

@Repository

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, UUID> {
    @Query("SELECT a FROM AvailabilitySlot a WHERE a.staff.id = :staffId " +
           "AND (a.startTime < :end AND a.endTime > :start) " +
           "AND a.status IN ('BOOKED', 'REST')")
    List<AvailabilitySlot> findByStaffIdAndOverlap(
        @Param("staffId") UUID staffId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );


    List<AvailabilitySlot> findByStaffIdAndStatusIn(UUID staffId, List<SlotStatus> statuses);

    Optional<AvailabilitySlot> findById(UUID id);

    void deleteByStaffIdAndStartTime(UUID staffId, Instant startTime);

    void deleteAllByStaffIdAndStartTimeIn(UUID staffId, List<Instant> startTimes);

<S extends AvailabilitySlot> List<S> saveAll(Iterable<S> entities);


List<AvailabilitySlot> findByStaffIdAndStartTimeIn(UUID staffId, List<Instant> startTimes);


    List<AvailabilitySlot> findByStaffIdAndStatus(UUID staffId, SlotStatus status);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AvailabilitySlot s " +
           "WHERE s.staff.id = :staffId " +
           "AND ((s.startTime < :end AND s.endTime > :start))")
    List<AvailabilitySlot> findByStaffIdAndOverlapWithLock(@Param("staffId") UUID staffId, 
                                                           @Param("start") Instant start, 
                                                           @Param("end") Instant end);
}
