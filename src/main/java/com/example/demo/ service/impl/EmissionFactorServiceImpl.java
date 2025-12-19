package com.example.demo.service.impl;

import com.example.demo.entity.ActivityType;
import com.example.demo.entity.EmissionFactor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ActivityTypeRepository;
import com.example.demo.repository.EmissionFactorRepository;
import com.example.demo.service.EmissionFactorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {
    
    private final EmissionFactorRepository factorRepository;
    private final ActivityTypeRepository typeRepository;
    
    // Constructor with EXACT parameter order: (EmissionFactorRepository, ActivityTypeRepository)
    public EmissionFactorServiceImpl(EmissionFactorRepository factorRepository,
                                    ActivityTypeRepository typeRepository) {
        this.factorRepository = factorRepository;
        this.typeRepository = typeRepository;
    }
    
    @Override
    public EmissionFactor createFactor(Long activityTypeId, EmissionFactor factor) {
        ActivityType activityType = typeRepository.findById(activityTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        if (factor.getFactorValue() == null || factor.getFactorValue() <= 0) {
            throw new ValidationException("Factor value must be greater than zero");
        }
        
        if (factor.getUnit() == null || factor.getUnit().trim().isEmpty()) {
            throw new ValidationException("Unit is required");
        }
        
        factor.setActivityType(activityType);
        return factorRepository.save(factor);
    }
    
    @Override
    public EmissionFactor getFactor(Long id) {
        return factorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emission factor not found"));
    }
    
    @Override
    public EmissionFactor getFactorByType(Long typeId) {
        return factorRepository.findByActivityType_Id(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Emission factor not found"));
    }
    
    @Override
    public List<EmissionFactor> getAllFactors() {
        return factorRepository.findAll();
    }
}   