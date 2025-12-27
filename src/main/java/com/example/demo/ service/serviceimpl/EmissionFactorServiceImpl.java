package com.example.demo.service.impl;

import com.example.demo.entity.ActivityType;
import com.example.demo.entity.EmissionFactor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ActivityTypeRepository;
import com.example.demo.repository.EmissionFactorRepository;
import com.example.demo.service.EmissionFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository factorRepository;
    private final ActivityTypeRepository typeRepository;
    
    @Autowired
    public EmissionFactorServiceImpl(EmissionFactorRepository factorRepository,
                                     ActivityTypeRepository typeRepository) {
        this.factorRepository = factorRepository;
        this.typeRepository = typeRepository;
    }
    
    @Override
    public EmissionFactor createFactor(Long activityTypeId, EmissionFactor factor) 
            throws ResourceNotFoundException, ValidationException {
        
        // Validate factor value
        if (factor.getFactorValue() == null || factor.getFactorValue() <= 0) {
            throw new ValidationException("Factor value must be greater than zero");
        }
        
        if (factor.getUnit() == null || factor.getUnit().trim().isEmpty()) {
            throw new ValidationException("Unit is required");
        }
        
        // Find activity type
        ActivityType activityType = typeRepository.findById(activityTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type not found"));
        
        // Check if factor already exists for this type
        if (factorRepository.findByActivityType_Id(activityTypeId).isPresent()) {
            throw new ValidationException("Emission factor already configured for this activity type");
        }
        
        // Set properties
        factor.setActivityType(activityType);
        factor.setCreatedAt(LocalDateTime.now());
        
        return factorRepository.save(factor);
    }
    
    @Override
    public EmissionFactor getFactor(Long id) throws ResourceNotFoundException {
        return factorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emission factor not found"));
    }
    
    @Override
    public EmissionFactor getFactorByType(Long typeId) throws ResourceNotFoundException {
        return factorRepository.findByActivityType_Id(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Emission factor not found"));
    }
    
    @Override
    public List<EmissionFactor> getAllFactors() {
        return factorRepository.findAll();
    }
}