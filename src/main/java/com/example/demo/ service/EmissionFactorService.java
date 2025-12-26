package com.example.demo.service;

import com.example.demo.entity.EmissionFactor;

import java.util.List;

public interface EmissionFactorService {

    EmissionFactor getFactorByType(Long typeId);

    List<EmissionFactor> getAllFactors();
}
