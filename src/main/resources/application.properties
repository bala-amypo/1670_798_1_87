package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factors")
@Tag(name = "Emission Factors", description = "Emission factor management endpoints")
public class EmissionFactorController {
    
    private final EmissionFactorService factorService;
    
    public EmissionFactorController(EmissionFactorService factorService) {
        this.factorService = factorService;
    }
    
    @PostMapping("/{activityTypeId}")
    @Operation(summary = "Create a new emission factor for an activity type")
    public EmissionFactor createFactor(@PathVariable Long activityTypeId, @RequestBody EmissionFactor factor) {
        return factorService.createFactor(activityTypeId, factor);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get emission factor by ID")
    public EmissionFactor getFactor(@PathVariable Long id) {
        return factorService.getFactor(id);
    }
    
    @GetMapping("/type/{activityTypeId}")
    @Operation(summary = "Get emission factor by activity type ID")
    public EmissionFactor getFactorByType(@PathVariable Long activityTypeId) {
        return factorService.getFactorByType(activityTypeId);
    }
    
    @GetMapping
    @Operation(summary = "Get all emission factors")
    public List<EmissionFactor> getAllFactors() {
        return factorService.getAllFactors();
    }
}