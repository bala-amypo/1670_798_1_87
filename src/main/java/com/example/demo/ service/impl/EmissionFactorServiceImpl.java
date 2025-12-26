package com.example.demo.service.impl;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmissionFactorRepository;
import com.example.demo.service.EmissionFactorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {

    private final EmissionFactorRepository factorRepository;

    public EmissionFactorServiceImpl(
            EmissionFactorRepository factorRepository) {
        this.factorRepository = factorRepository;
    }

    @Override
    public EmissionFactor getFactorByType(Long typeId) {
        return factorRepository.findByActivityType_Id(typeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Emission factor not found"));
    }

    @Override
    public List<EmissionFactor> getAllFactors() {
        return factorRepository.findAll();
    }
}
