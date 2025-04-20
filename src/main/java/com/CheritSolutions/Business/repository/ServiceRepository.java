package com.CheritSolutions.Business.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Business.entity.Servicee;
@Repository
public interface ServiceRepository extends JpaRepository<Servicee, UUID> {
    List<Servicee> findByBusinessId(UUID businessId);

    
    @Query("SELECT s FROM Servicee s WHERE s.business.id = :businessId " +
       "AND (:name IS NULL OR LOWER(s.name) LIKE %:name%) " +
       "AND (:minPrice IS NULL OR s.basePrice >= :minPrice) " +
       "AND (:maxPrice IS NULL OR s.basePrice <= :maxPrice)")
List<Servicee> findByBusinessIdWithFilters(
    @Param("businessId") UUID businessId,
    @Param("name") String name,
    @Param("minPrice") BigDecimal minPrice,
    @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT s FROM Servicee s WHERE " +
    "(:name IS NULL OR LOWER(s.name) LIKE %:name%) " +
    "AND (:minPrice IS NULL OR s.basePrice >= :minPrice) " +
    "AND (:maxPrice IS NULL OR s.basePrice <= :maxPrice)")
List<Servicee> findAllWithFilters(
 @Param("name") String name,
 @Param("minPrice") BigDecimal minPrice,
 @Param("maxPrice") BigDecimal maxPrice);
}
