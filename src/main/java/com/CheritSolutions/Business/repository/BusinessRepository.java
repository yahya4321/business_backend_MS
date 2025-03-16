package com.CheritSolutions.Business.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Business.entity.Business;

@Repository

public interface BusinessRepository extends JpaRepository<Business, UUID> {}

