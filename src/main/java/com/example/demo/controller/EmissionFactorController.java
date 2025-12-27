package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factors")
public class EmissionFactorController {
    
    @Autowired
    private EmissionFactorService factorService;
    
    @PostMapping("/type/{activityTypeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmissionFactor> createFactor(@PathVariable Long activityTypeId,
                                                       @Valid @RequestBody EmissionFactor factor) {
        EmissionFactor created = factorService.createFactor(activityTypeId, factor);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmissionFactor> getFactor(@PathVariable Long id) {
        EmissionFactor factor = factorService.getFactor(id);
        return ResponseEntity.ok(factor);
    }
    
    @GetMapping("/type/{typeId}")
    public ResponseEntity<EmissionFactor> getFactorByType(@PathVariable Long typeId) {
        EmissionFactor factor = factorService.getFactorByType(typeId);
        return ResponseEntity.ok(factor);
    }
    
    @GetMapping
    public ResponseEntity<List<EmissionFactor>> getAllFactors() {
        List<EmissionFactor> factors = factorService.getAllFactors();
        return ResponseEntity.ok(factors);
    }
}