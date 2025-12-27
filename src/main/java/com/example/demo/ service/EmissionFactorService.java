package com.example.demo.service;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;

import java.util.List;

public interface EmissionFactorService {
    EmissionFactor createFactor(Long activityTypeId, EmissionFactor factor) 
            throws ResourceNotFoundException, ValidationException;
    EmissionFactor getFactor(Long id) throws ResourceNotFoundException;
    EmissionFactor getFactorByType(Long typeId) throws ResourceNotFoundException;
    List<EmissionFactor> getAllFactors();
}