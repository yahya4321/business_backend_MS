package com.CheritSolutions.Business.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Business.entity.Servicee;
@Repository
public interface ServiceRepository extends JpaRepository<Servicee, UUID> {
    List<Servicee> findByBusinessId(UUID businessId);
}
