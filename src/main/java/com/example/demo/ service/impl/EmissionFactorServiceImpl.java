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

    private final EmissionFactorRepository emissionFactorRepository;
    private final ActivityTypeRepository activityTypeRepository;

    // 🔴 Constructor injection order MUST MATCH test suite
    public EmissionFactorServiceImpl(
            EmissionFactorRepository emissionFactorRepository,
            ActivityTypeRepository activityTypeRepository) {
        this.emissionFactorRepository = emissionFactorRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @Override
    public EmissionFactor createFactor(Long activityTypeId, EmissionFactor factor) {

        if (factor.getFactorValue() == null || factor.getFactorValue() <= 0) {
            throw new ValidationException("Emission factor not found");
        }

        ActivityType activityType = activityTypeRepository.findById(activityTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        factor.setActivityType(activityType);

        return emissionFactorRepository.save(factor);
    }

    @Override
    public EmissionFactor getFactor(Long id) {
        return emissionFactorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Emission factor not found"));
    }

    @Override
    public EmissionFactor getFactorByType(Long typeId) {
        return emissionFactorRepository.findByActivityType_Id(typeId)
                .orElseThrow(() ->
                        new ValidationException("No emission factor configured"));
    }

    @Override
    public List<EmissionFactor> getAllFactors() {
        return emissionFactorRepository.findAll();
    }
}
