package com.CheritSolutions.Business.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Business.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    List<Staff> findByBusinessId(UUID businessId);
    List<Staff> findByServiceId(UUID serviceId);
    Optional<Staff> findByIdAndBusinessId(UUID staffId, UUID businessId);
}