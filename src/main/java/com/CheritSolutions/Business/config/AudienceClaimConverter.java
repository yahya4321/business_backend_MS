package com.CheritSolutions.Business.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

public class AudienceClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        // Allow tokens issued for Booking_Backend and Business_Backend
        claims.put("aud", Arrays.asList("Business_Backend", "Booking_Backend"));
        return claims;
    }
}
