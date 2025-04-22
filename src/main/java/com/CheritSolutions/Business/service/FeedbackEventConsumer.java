package com.CheritSolutions.Business.service;

import com.CheritSolutions.Business.entity.Servicee;
import com.CheritSolutions.Business.entity.Staff;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeedbackEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackEventConsumer.class);

    @PersistenceContext
    private EntityManager entityManager;

    @KafkaListener(topics = "feedback.created", groupId = "business-service")
    @Transactional
    public void consumeFeedbackEvent(FeedbackEvent event) {
        if (event == null) {
            logger.error("Received null feedback event");
            return;
        }

        logger.info("Received feedback event: feedbackId={}", event.getFeedbackId());

        try {
            // Update Servicee rating
            if (event.getServiceId() != null && event.getServiceRating() != null) {
                Servicee service = entityManager.find(Servicee.class, event.getServiceId());
                if (service != null) {
                    updateRating(service, event.getServiceRating());
                    entityManager.persist(service);
                    logger.info("Updated rating for serviceId={}", event.getServiceId());
                } else {
                    logger.warn("Service not found for serviceId={}", event.getServiceId());
                }
            }

            // Update Staff rating
            if (event.getStaffId() != null && event.getStaffRating() != null) {
                Staff staff = entityManager.find(Staff.class, event.getStaffId());
                if (staff != null) {
                    updateRating(staff, event.getStaffRating());
                    entityManager.persist(staff);
                    logger.info("Updated rating for staffId={}", event.getStaffId());
                } else {
                    logger.warn("Staff not found for staffId={}", event.getStaffId());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to process feedback event: feedbackId={}, error={}", event.getFeedbackId(), e.getMessage(), e);
            throw new RuntimeException("Failed to process feedback event", e);
        }
    }

    private void updateRating(Object entity, Integer newRating) {
        BigDecimal currentAverage = BigDecimal.ZERO;
        Integer currentCount = 0;

        if (entity instanceof Servicee service) {
            currentAverage = service.getAverageRating() != null ? service.getAverageRating() : BigDecimal.ZERO;
            currentCount = service.getRatingCount() != null ? service.getRatingCount() : 0;
        } else if (entity instanceof Staff staff) {
            currentAverage = staff.getAverageRating() != null ? staff.getAverageRating() : BigDecimal.ZERO;
            currentCount = staff.getRatingCount() != null ? staff.getRatingCount() : 0;
        }

        BigDecimal totalRating = currentAverage.multiply(BigDecimal.valueOf(currentCount)).add(BigDecimal.valueOf(newRating));
        int newCount = currentCount + 1;
        BigDecimal newAverage = totalRating.divide(BigDecimal.valueOf(newCount), 2, RoundingMode.HALF_UP);

        if (entity instanceof Servicee service) {
            service.setAverageRating(newAverage);
            service.setRatingCount(newCount);
        } else if (entity instanceof Staff staff) {
            staff.setAverageRating(newAverage);
            staff.setRatingCount(newCount); // Fixed bug: was incorrectly setting service.ratingCount
        }
    }
}