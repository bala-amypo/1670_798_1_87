package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Emission Factors")
@RestController
@RequestMapping("/api/factors")
public class EmissionFactorController {

    private final EmissionFactorService factorService;

    public EmissionFactorController(EmissionFactorService factorService) {
        this.factorService = factorService;
    }

    @PostMapping("/{activityTypeId}")
    public EmissionFactor create(@PathVariable Long activityTypeId,
                                 @RequestBody EmissionFactor factor) {
        return factorService.createFactor(activityTypeId, factor);
    }

    @GetMapping("/{id}")
    public EmissionFactor get(@PathVariable Long id) {
        return factorService.getFactor(id);
    }

    @GetMapping("/type/{activityTypeId}")
    public EmissionFactor getByType(@PathVariable Long activityTypeId) {
        return factorService.getFactorByType(activityTypeId);
    }

    @GetMapping
    public List<EmissionFactor> getAll() {
        return factorService.getAllFactors();
    }
}
