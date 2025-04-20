package com.CheritSolutions.Business.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Keep this for @Configuration

import com.CheritSolutions.Business.dto.StaffRequest;
import com.CheritSolutions.Business.entity.Staff;



@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE); 

        mapper.addMappings(new PropertyMap<StaffRequest, Staff>() {
    @Override
    protected void configure() {
        skip().setId(null);
    }
});

            return mapper;
    }
}                                                   